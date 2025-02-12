package com.jediterm.terminal.model.hyperlinks;


import lombok.extern.slf4j.Slf4j;

/**
 * @author traff
 */
@Slf4j
public class LinkResultItem {
  private final int myStartOffset;
  private final int myEndOffset;

  private final LinkInfo myLinkInfo;

  public LinkResultItem(int startOffset, int endOffset,  LinkInfo linkInfo) {
    myStartOffset = startOffset;
    myEndOffset = endOffset;
    myLinkInfo = linkInfo;
  }

  public int getStartOffset() {
    return myStartOffset;
  }

  public int getEndOffset() {
    return myEndOffset;
  }

  public LinkInfo getLinkInfo() {
    return myLinkInfo;
  }
}
