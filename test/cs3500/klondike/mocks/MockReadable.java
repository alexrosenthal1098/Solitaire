package cs3500.klondike.mocks;

import java.io.IOException;
import java.nio.CharBuffer;

/**
 * A mock class that implements readable and is used for testing.
 * All methods throw an I/O exception.
 */
public class MockReadable implements Readable {

  @Override
  public int read(CharBuffer cb) throws IOException {
    throw new IOException("Mock exception");
  }
}
