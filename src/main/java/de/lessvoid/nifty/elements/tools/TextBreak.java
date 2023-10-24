package de.lessvoid.nifty.elements.tools;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lessvoid.nifty.spi.render.RenderFont;

/**
 * The Class TextBreak.
 */
public class TextBreak {
  
  /** The words. */
  @Nonnull
  private final String[] words;
  
  /** The width. */
  private final int width;
  
  /** The font. */
  private final RenderFont font;

  /**
	 * Instantiates a new text break.
	 *
	 * @param line  the line
	 * @param width the width
	 * @param font  the font
	 */
  public TextBreak(@Nonnull final String line, final int width, final RenderFont font) {
    this.words = line.split(" ", -1);
    this.width = width;
    this.font = font;
  }

  /**
	 * Split.
	 *
	 * @return the list
	 */
  @Nonnull
  public List<String> split() {
    if (isSingleLine()) {
      return singleResult();
    }
    return processWords();
  }

  /**
	 * Single result.
	 *
	 * @return the list
	 */
  @Nonnull
  private List<String> singleResult() {
    List<String> result = new ArrayList<String>();
    result.add(words[0]);
    return result;
  }

  /**
	 * Process words.
	 *
	 * @return the list
	 */
  @Nonnull
  private List<String> processWords() {
    List<String> result = new ArrayList<String>();
    int i = 0, length;
    String currentWord = "";
    String lastColorValue = null;
    StringBuilder currentLine = new StringBuilder();
    while (isValidIndex(i)) {
      //Empty StringBuffer
      currentLine.setLength(0);
      length = 0;
      while (isBelowLimit(length) && isValidIndex(i)) {
        currentWord = getWord(i, length == 0);
        String colorValue = extractColorValue(currentWord);
        if (colorValue != null) {
          lastColorValue = colorValue;
        }
        length += font.getWidth(currentWord);
        if (isBelowLimit(length)) {
          currentLine.append(currentWord);
          i++;
        }
      }
      if (currentLine.length() > 0) {
        addResult(result, lastColorValue, currentLine.toString());
      } else { //If we get here the word itself is longer than the wrapping width
        //We break it up
        String wordPart = currentWord;
        int p;
        do {
          p = 0;
          while (!isBelowLimit(font.getWidth(wordPart)) && wordPart.length() > 0) {
            //Remove one character from the end and see if the new word fits
            wordPart = wordPart.substring(0, wordPart.length() - 1);
            p++;
          }
          addResult(result, lastColorValue, wordPart);
          //Set the new word part to the rest of the word
          wordPart = currentWord.substring(currentWord.length() - p);
          String colorValue = extractColorValue(wordPart);
          if (colorValue != null) {
            lastColorValue = colorValue;
          }
        } while (p > 0);
        i++;
      }
    }
    return result;
  }

  /**
	 * Adds the result.
	 *
	 * @param result         the result
	 * @param lastColorValue the last color value
	 * @param currentLine    the current line
	 */
  private void addResult(
      @Nonnull final List<String> result,
      @Nullable final String lastColorValue,
      final String currentLine) {
    if (lastColorValue != null) {
      result.add(lastColorValue + currentLine);
    } else {
      result.add(currentLine);
    }
  }

  /**
	 * Checks if is valid index.
	 *
	 * @param i the i
	 * @return true, if is valid index
	 */
  private boolean isValidIndex(final int i) {
    return i < words.length;
  }

  /**
	 * Checks if is below limit.
	 *
	 * @param currentLineLength the current line length
	 * @return true, if is below limit
	 */
  private boolean isBelowLimit(final int currentLineLength) {
    return currentLineLength < width;
  }

  /**
	 * Gets the word.
	 *
	 * @param i       the i
	 * @param newLine the new line
	 * @return the word
	 */
  private String getWord(final int i, final boolean newLine) {
    String currentWord = words[i];
    if (i > 0 && !newLine) {
      currentWord = " " + currentWord;
    }
    return currentWord;
  }

  /**
	 * Checks if is single line.
	 *
	 * @return true, if is single line
	 */
  private boolean isSingleLine() {
    //Check if there is only one word and it fits in one line
    return (words.length == 1 && isBelowLimit(font.getWidth(words[0])));
  }

  /**
	 * Extract color value.
	 *
	 * @param text the text
	 * @return the string
	 */
  @Nullable
  String extractColorValue(@Nullable final String text) {
    if (text == null) {
      return null;
    }
    int start = text.lastIndexOf("\\#");
    if (start != -1) {
      int end = text.indexOf("#", start + 2);
      if (end != -1) {
        return text.substring(start, end + 1);
      }
    }
    return null;
  }
}
