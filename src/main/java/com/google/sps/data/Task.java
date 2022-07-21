package com.google.sps.data;

/** An item on a todo list. */
public final class Task {

  private final long id;
  private final String title;
  private final long timestamp;
  private final String duration;

  public Task(long id, String title, String duration, long timestamp) {
    this.id = id;
    this.title = title;
    this.duration = duration;
    this.timestamp = timestamp;
  }
}