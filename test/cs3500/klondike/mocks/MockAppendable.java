package cs3500.klondike.mocks;

import java.io.IOException;

/**
 * A mock class that implements appendable and is used for testing.
 * All methods throw an IOException.
 */
public class MockAppendable implements Appendable {

  @Override
  public Appendable append(CharSequence csq) throws IOException {
    throw new IOException("Mock exception");
  }

  @Override
  public Appendable append(CharSequence csq, int start, int end) throws IOException {
    throw new IOException("Mock exception");
  }

  @Override
  public Appendable append(char c) throws IOException {
    throw new IOException("Mock exception");
  }
}
