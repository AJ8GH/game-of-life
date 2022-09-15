package io.github.aj8gh.gameoflife.application;

import static java.lang.Thread.sleep;

import io.github.aj8gh.gameoflife.domain.Grid;
import io.github.aj8gh.gameoflife.seeder.Seeder;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Setter
@Getter
@AllArgsConstructor
public class Game {

  private static final Logger LOG = LoggerFactory.getLogger(Game.class.getName());

  private final Executor executor = Executors.newFixedThreadPool(6);
  private final AtomicInteger generation = new AtomicInteger(0);
  private final AtomicBoolean running = new AtomicBoolean(false);
  private final Consumer<Game> consumerAdaptor;
  private final Seeder seeder;
  private final Grid grid;

  private int tickDuration;

  public void run() {
    if (!running.get()) {
      this.running.getAndSet(true);
      LOG.info("*** Game Started ***");
    } else {
      throw new IllegalStateException("Game already running");
    }
    executor.execute(() -> {
      while (running.get()) {
        consumerAdaptor.accept(this);
        if (extinct()) {
          running.getAndSet(false);
          break;
        }
        tick();
      }
    });
  }

  public void stop() {
    if (running.get()) {
      this.running.getAndSet(false);
      LOG.info("*** Game Stopped ***");
      return;
    }
    throw new IllegalStateException("Game already stopped");
  }

  private void tick() {
    if (!isRunning()) {
      return;
    }
    grid.tick();
    generation.incrementAndGet();
    try {
      sleep(tickDuration);
    } catch (InterruptedException e) {
      LOG.error("Thread interrupted", e);
      Thread.currentThread().interrupt();
    }
  }

  public void reset() {
    synchronized (this) {
      if (isRunning()) {
        stop();
      }
      synchronized (seeder) {
        grid.setCells(seeder.seed());
      }
      generation.getAndSet(0);
      LOG.info("*** Game Reset ***");
    }
  }

  public void setTickDuration(int tickDuration) {
    if (tickDuration < 0) {
      throw new IllegalArgumentException("Tick Duration must be >= 0");
    }
    this.tickDuration = tickDuration;
  }

  public int getGeneration() {
    return generation.get();
  }

  public long population() {
    return grid.population();
  }

  public boolean isRunning() {
    return running.get();
  }

  private boolean extinct() {
    return population() == 0;
  }
}
