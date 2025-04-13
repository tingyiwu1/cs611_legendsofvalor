package src.service.screens.ScreenInterfaces;

// represents process that interacts with the user and returns a result
public interface Process<T extends Process.Result> {
  public static interface Result {
  }

  T run();
}
