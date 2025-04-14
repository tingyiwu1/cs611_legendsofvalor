package src.service.screens;

import java.util.Optional;

import src.service.screens.ScreenInterfaces.Process;

public class ScreenResult<T> implements Process.Result {
  private static final ScreenResult<?> QUIT = new ScreenResult<>(null, Kind.QUIT);
  private static final ScreenResult<?> GO_BACK = new ScreenResult<>(null, Kind.GO_BACK);

  private enum Kind {
    SUCCESS, QUIT, GO_BACK
  }

  // we need to use Optional here because we need to distinguish between
  // ScreenResult<Void> screenResult = ScreenResult.success(null)
  // and ScreenResult<?> screenResult = ScreenResult.quit()
  private final Optional<T> result;
  private final Kind kind;

  private ScreenResult(Optional<T> result, Kind kind) {
    // either success with result or not success with null result
    assert (kind == Kind.SUCCESS) == (result == null);

    this.result = result;
    this.kind = kind;
  }

  public static <T> ScreenResult<T> success(T result) {
    return new ScreenResult<>(Optional.ofNullable(result), Kind.SUCCESS);
  }

  public static <T> ScreenResult<T> quit() {
    @SuppressWarnings("unchecked")
    ScreenResult<T> t = (ScreenResult<T>) QUIT;
    return t;
  }

  public static <T> ScreenResult<T> goBack() {
    @SuppressWarnings("unchecked")
    ScreenResult<T> t = (ScreenResult<T>) GO_BACK;
    return t;
  }

  public T getResult() {
    if (this.kind != Kind.SUCCESS) {
      throw new IllegalStateException("ScreenResult is not success");
    }
    return this.result.orElse(null);
  }

  public boolean isQuit() {
    return this.kind == Kind.QUIT;
  }

  public boolean isGoBack() {
    return this.kind == Kind.GO_BACK;
  }
}
