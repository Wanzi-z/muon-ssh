package com.jediterm.terminal.util;

import lombok.extern.slf4j.Slf4j;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
* @author traff
*/
@Slf4j
public class JTextFieldLimit extends PlainDocument {
  private final int limit;

  public JTextFieldLimit(int limit) {
    super();
    this.limit = limit;
  }

  public void insertString( int offset, String  str, AttributeSet attr ) throws BadLocationException {
    if (str == null) return;

    if ((getLength() + str.length()) <= limit) {
      super.insertString(offset, str, attr);
    }
  }
}
