# Project Title

frakt will explore fractal patterns using various generation techniques.
Currently frakt only implements the algorithm descibed here:
[The Chaos Game](https://beltoforion.de/en/recreational_mathematics/chaos_game.php#idStart)
  1. Start with a triangle with vertices v0, v1, v2
  2. Choose a random point within the triangle p
  3. Choose a random vertice v∈{v0, v1, v2}
  4. Determine the midpoint p1 between p and v
  5. Plot the point p1
  6. Assign p to p1 and goto 3


## Requirements

- Clojure cli tool (clj, clojure)
- Image viewer that can view .ppm images (feh, emacs)

## Installation

1. Just clone the repo

   ```sh
   git clone my-repo
   ```

## Usage

Examples may be run using the clojure cli tool. All examplese should include
a main function. To run the sierpinski.clj example type:

   ```sh
   clj -X frakt.sierpinski/main
   ```

A file will be generated in the root directory called `sierpinski.ppm`. Open
this file in feh using:

    ```sh
	feh sierpinski.ppm
	```

## Related Projects


## Credits

Thank those who helped make this possible.

## License

[MIT](LICENSE) © [gmrowe](https://github.com/gmrowe).
