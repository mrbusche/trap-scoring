import glob
import os
import pandas as pd
import sys
import warnings

# Suppress specific openpyxl warnings about unparseable headers/footers
warnings.filterwarnings('ignore', message='Cannot parse header or footer')


def compare_excel_files(file1_path, file2_path):
    """
    Compares two Excel files.
    1. Checks structure/sheet names.
    2. Checks strict equality (same order).
    3. If strict equality fails, sorts data to check if content is identical.
    """

    if not os.path.exists(file1_path):
        print(f'Error: File not found -> {file1_path}')
        return
    if not os.path.exists(file2_path):
        print(f'Error: File not found -> {file2_path}')
        return

    print(f'--- Starting Comparison ---')
    print(f'File A: {file1_path}')
    print(f'File B: {file2_path}')
    print('---------------------------\n')

    try:
        xl1 = pd.ExcelFile(file1_path)
        xl2 = pd.ExcelFile(file2_path)
    except Exception as e:
        print(f'Critical Error reading Excel files: {e}')
        return

    # Compare Sheet Names
    sheets1 = set(xl1.sheet_names)
    sheets2 = set(xl2.sheet_names)

    if sheets1 != sheets2:
        print('x Sheet mismatch detected!')
        only_in_1 = sheets1 - sheets2
        only_in_2 = sheets2 - sheets1
        if only_in_1:
            print(f'  - Sheets only in File A: {only_in_1}')
        if only_in_2:
            print(f'  - Sheets only in File B: {only_in_2}')
    else:
        print('✓ Sheet names match.')

    common_sheets = sheets1.intersection(sheets2)

    differences_found = False

    for sheet in sorted(common_sheets):
        print(f"\nAnalyzing Sheet: '{sheet}'...")

        try:
            df1 = xl1.parse(sheet, header=None)
            df2 = xl2.parse(sheet, header=None)

            # Normalize "As of:" cells to ignore date differences
            df1 = df1.replace(to_replace=r'As of:.*', value='As of: IGNORED', regex=True)
            df2 = df2.replace(to_replace=r'As of:.*', value='As of: IGNORED', regex=True)
        except Exception as e:
            print(f"  x Error parsing sheet '{sheet}': {e}")
            continue

        # Compare Dimensions
        if df1.shape != df2.shape:
            print(f'  x Dimension mismatch:')
            print(f'    File A: {df1.shape[0]} rows, {df1.shape[1]} columns')
            print(f'    File B: {df2.shape[0]} rows, {df2.shape[1]} columns')
            differences_found = True
            continue

        # Strict Value Comparison (Order matters)
        if df1.equals(df2):
            print('  ✓ Identical (Order and Content match)')
        else:
            # Smart Comparison (Ignore Sort Order)
            print('  ! Strict mismatch found. Checking if data is just sorted differently...')

            # Sort by all columns to align data
            # convert to string temporarily to avoid sorting errors with mixed types (int vs str)
            df1_sorted = df1.sort_values(by=df1.columns.tolist(), key=lambda col: col.astype(str)).reset_index(
                drop=True
            )
            df2_sorted = df2.sort_values(by=df2.columns.tolist(), key=lambda col: col.astype(str)).reset_index(
                drop=True
            )

            if df1_sorted.equals(df2_sorted):
                print('  ✓ Identical Content (Data matches, but sort order was different)')
            else:
                print('  x True content differences found.')
                differences_found = True

                try:
                    diff = df1_sorted.compare(df2_sorted, align_axis=0)

                    print(f'    First 10 differences (after aligning rows):')
                    print(diff.head(10).to_string())

                    if len(diff) > 10:
                        print(f'\n    ... {len(diff) - 10} more differences hidden.')
                except Exception as e:
                    print(f'    Could not generate detailed diff report: {e}')

    print('\n---------------------------')
    if differences_found:
        print('RESULT: Files are DIFFERENT.')
        sys.exit(1)
    else:
        print('RESULT: Files match (Content is identical).')
        sys.exit(0)


if __name__ == '__main__':
    file1 = sys.argv[1]

    search_pattern = os.path.join('..', 'league-data-*.xlsx')
    candidates = glob.glob(search_pattern)

    if not candidates:
        print(f"Error: No files matching '{search_pattern}' found.")
        sys.exit(1)

    # Sort by modification time to get the newest
    try:
        newest_file = max(candidates, key=os.path.getmtime)
        print(f'(Auto-selected baseline file: {newest_file})')
        compare_excel_files(file1, newest_file)
    except Exception as e:
        print(f'Error determining newest file: {e}')
        sys.exit(1)
