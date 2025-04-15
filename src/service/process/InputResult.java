package src.service.process;

import java.util.NoSuchElementException;

/**
 * Represent the result of InputProcess. It can be either a valid result or a T
 * 
 * Implementation is heavily based on the Java Optional class.
 */
public class InputResult<T> implements Process.Result {
  private static final InputResult<?> INVALID = new InputResult<>(null);

  private final T result;

  public static <T> InputResult<T> invalid() {
    @SuppressWarnings("unchecked")
    InputResult<T> t = (InputResult<T>) INVALID;
    return t;
  }

  public static <T> InputResult<T> of(T result) {
    return new InputResult<>(result);
  }

  public T getResult() {
    if (this.result == null) {
      throw new NoSuchElementException("InputSignal is quit");
    }
    return this.result;
  }

  public boolean isInvalid() {
    return this.result == null;
  }

  private InputResult(T result) {
    this.result = result;
  }

}
