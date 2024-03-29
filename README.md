# Game of Life

[![Build Status](https://app.travis-ci.com/AJ8GH/game-of-life.svg?branch=main)](https://app.travis-ci.com/AJ8GH/game-of-life)
[![Maintainability](https://api.codeclimate.com/v1/badges/06d103b78f8ff34fe36e/maintainability)](https://codeclimate.com/github/AJ8GH/game-of-life/maintainability)
[![Coverage](https://codecov.io/gh/AJ8GH/game-of-life/branch/main/graph/badge.svg?token=16E8EXA7A6)](https://codecov.io/gh/AJ8GH/game-of-life)
[![BCH compliance](https://bettercodehub.com/edge/badge/AJ8GH/game-of-life?branch=main)](https://bettercodehub.com/)

[Conway's Game of Life](https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life) implemented and test
driven in Java

## Getting Started

### Docker
1. Start Docker
2. Build image
```shell
docker build .
```
3. Run container exposing port 8080 to allow requests to the REST API
```docker
docker run -p 8080:8080 <image-id>
```

### Build & Run Locally

1. Install Java 17
2. Install Maven 3
3. Build project:
```shell
mvn clean install
```
4. Run application jar
```shell
java -jar launcher/target/gol.jar
```


## Rules

The universe of the Game of Life is an infinite,
two-dimensional orthogonal grid of square cells,
each of which is in one of two possible states,
live or dead, (or populated and unpopulated, respectively).

Every cell interacts with its eight neighbours,
which are the cells that are horizontally, vertically,
or diagonally adjacent. At each step in time, the following transitions occur:

1. Any live cell with fewer than two live neighbours dies, as if by underpopulation.
2. Any live cell with two or three live neighbours lives on to the next generation.
3. Any live cell with more than three live neighbours dies, as if by overpopulation.
4. Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.

These rules, which compare the behavior of the automaton to real life, can be condensed into the
following:

1. Any live cell with two or three live neighbours survives.
2. Any dead cell with three live neighbours becomes a live cell.
3. All other live cells die in the next generation. Similarly, all other dead cells stay dead.

The initial pattern constitutes the seed of the system.
The first generation is created by applying the above rules simultaneously to every cell in the
seed,
live or dead; births and deaths occur simultaneously,
and the discrete moment at which this happens is sometimes called a tick.
Each generation is a pure function of the preceding one.
The rules continue to be applied repeatedly to create further generations.

## Classes

- Cell
- Grid

## Rest API endpoints:

- `/enqueue`
- `/dequeue`
- `/start`
- `/stop`
- `/reset`

### Example request body for /queue endpoint

```json
{
  "population": 5,
  "generation": 40,
  "grid": [
    [
      {
        "alive": false
      },
      {
        "alive": true
      },
      {
        "alive": true
      }
    ],
    [
      {
        "alive": true
      },
      {
        "alive": false
      },
      {
        "alive": true
      }
    ],
    [
      {
        "alive": true
      },
      {
        "alive": false
      },
      {
        "alive": false
      }
    ]
  ]
}
```

### Example curl request to /queue to enqueue a gameState object

```shell
curl -X POST http://localhost:8080/queue \
-H 'Content-Type: application/json' \
-d '{"population":5,"generation":40,"grid":[[{"alive":false},{"alive":true},{"alive":true}],[{"alive":true},{"alive":false},{"alive":true}],[{"alive":true},{"alive":false},{"alive":false}]]}'
```

### Example curl request to /send to dequeue and retrieve a gameState object

```shell
curl -X POST http://localhost:8080/send \
-H 'Content-Type: application/json' \
-d '{}'
```

## Profiles

- `local`
  _Runs game locally without start API or Spring web app_

- `lazy`
  _Starts API without starting game_

- `test`
  _Loads test properties file_

To run with profile, use VM the argument:
`-Dspring.profiles.active=<profile>`
