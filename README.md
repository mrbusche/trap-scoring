trap app

- Buid the jar using `gradle bootJar`
- Place that jar in a folder `c:\trap`.
- Add `singles.csv`, `doubles.csv`, `handicap.csv`, `trap.csv`, and `clays.csv` to that folder
- open command prompt
- type `cd c:\trap`
- type `java -Xms1024m -Xmx4096m -jar trap-0.1.0.jar`
- There will be output as the process runs. Depending on how fast your computer is it will take between 5 and 15 minutes.
- When finished there will be output of `Finished in 352211ms`
- There will be a file generated in `c:\trap` directory
