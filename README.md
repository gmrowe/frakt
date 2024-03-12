# Project Title

frakt will explore fractal patterns using various generation techniques.
Currently frakt only implements the algorithm descibed here:
[The Chaos Game](https://orgmode.org/guide/Hyperlinks.html)
  1. Start with a triangle with vertices v0, v1, v2
  2. Choose a random point within the triangle p
  3. Choose a random vertice v∈{v0, v1, v2}
  4. Determine the midpoint p1 between p and v
  5. Assign p to p1 and goto 3


## Requirements

- Clojure cli tool (clj, clojure)
- Image viewer that can view .ppm images (feh, emacs)

## Installation

1. Just clone the repo

   ```sh
   git clone my-repo
   ```

## Usage

Run using the clojure cli tool:

    ```sh
	clj -X frakt.core/main && feh frakt-core.ppm
	```

## Related Projects


## Credits

Thank those who helped make this possible.

## License

[MIT](LICENSE) © [gmrowe](https://github.com/gmrowe).
