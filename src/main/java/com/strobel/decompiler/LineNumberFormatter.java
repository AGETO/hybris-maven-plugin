package com.strobel.decompiler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import com.strobel.decompiler.languages.LineNumberPosition;

class LineNumberFormatter {
    private final List<LineNumberPosition>                      _positions;
    private final File                                          _file;
    private final EnumSet<LineNumberFormatter.LineNumberOption> _options;

    LineNumberFormatter(File file, List<LineNumberPosition> lineNumberPositions,
            EnumSet<LineNumberFormatter.LineNumberOption> options) {
        this._file = file;
        this._positions = lineNumberPositions;
        this._options = options == null ? EnumSet.noneOf(LineNumberFormatter.LineNumberOption.class) : options;
    }

    void reformatFile() throws IOException {
        final List<LineNumberPosition> lineBrokenPositions = new ArrayList<>();
        final List<String> brokenLines = this.breakLines(lineBrokenPositions);
        this.emitFormatted(brokenLines, lineBrokenPositions);
    }

    private List<String> breakLines(List<LineNumberPosition> o_LineBrokenPositions) throws IOException {
        int numLinesRead = 0;
        int lineOffset = 0;
        final List<String> brokenLines = new ArrayList<>();
        try (BufferedReader r = new BufferedReader(new FileReader(this._file))) {
            for (int x2 = 0; x2 < this._positions.size(); ++x2) {
                LineNumberPosition pos = this._positions.get(x2);
                o_LineBrokenPositions.add(
                        new LineNumberPosition(pos.getOriginalLine(), pos.getEmittedLine() + lineOffset, pos.getEmittedColumn()));

                while (numLinesRead < pos.getEmittedLine() - 1) {
                    brokenLines.add(r.readLine());
                    ++numLinesRead;
                }

                String line = r.readLine();
                ++numLinesRead;
                int prevPartLen = 0;
                char[] indent = new char[0];

                LineNumberPosition nextPos;
                do {
                    nextPos = x2 < this._positions.size() - 1 ? this._positions.get(x2 + 1) : null;
                    if (nextPos != null && nextPos.getEmittedLine() == pos.getEmittedLine()
                            && nextPos.getOriginalLine() > pos.getOriginalLine()) {
                        ++x2;
                        ++lineOffset;
                        String firstPart = line.substring(0, nextPos.getEmittedColumn() - prevPartLen - 1);
                        brokenLines.add(new String(indent) + firstPart);
                        prevPartLen += firstPart.length();
                        indent = new char[prevPartLen];
                        Arrays.fill(indent, ' ');
                        line = line.substring(firstPart.length(), line.length());
                        o_LineBrokenPositions.add(new LineNumberPosition(nextPos.getOriginalLine(),
                                nextPos.getEmittedLine() + lineOffset, nextPos.getEmittedColumn()));
                    } else {
                        nextPos = null;
                    }
                } while (nextPos != null);

                brokenLines.add(new String(indent) + line);
            }

            String var23;
            while ((var23 = r.readLine()) != null) {
                brokenLines.add(var23);
            }
        } catch (Throwable var21) {
            throw var21;
        }

        return brokenLines;
    }

    private void emitFormatted(List<String> brokenLines, List<LineNumberPosition> lineBrokenPositions) throws IOException {
        File tempFile = new File(this._file.getAbsolutePath() + ".fixed");
        int globalOffset = 0;
        int numLinesRead = 0;
        Iterator lines = brokenLines.iterator();
        int maxLineNo = LineNumberPosition.computeMaxLineNumber(lineBrokenPositions);
        try (LineNumberPrintWriter w = new LineNumberPrintWriter(maxLineNo, new BufferedWriter(new FileWriter(tempFile)))) {
            Throwable var9 = null;
            if (!this._options.contains(LineNumberFormatter.LineNumberOption.LEADING_COMMENTS)) {
                w.suppressLineNumbers();
            }

            boolean x2 = this._options.contains(LineNumberFormatter.LineNumberOption.STRETCHED);
            Iterator line = lineBrokenPositions.iterator();

            while (true) {
                label238: while (line.hasNext()) {
                    LineNumberPosition pos = (LineNumberPosition) line.next();
                    int nextTarget = pos.getOriginalLine();
                    int nextActual = pos.getEmittedLine();
                    int requiredAdjustment = nextTarget - nextActual - globalOffset;
                    if (x2 && requiredAdjustment < 0) {
                        final List<String> var30 = new ArrayList<>();

                        while (true) {
                            while (numLinesRead < nextActual - 1) {
                                String var31 = (String) lines.next();
                                ++numLinesRead;
                                if (requiredAdjustment < 0 && var31.trim().isEmpty()) {
                                    ++requiredAdjustment;
                                    --globalOffset;
                                } else {
                                    var30.add(var31);
                                }
                            }

                            int var32 = var30.size() + requiredAdjustment <= 0 ? nextTarget : -1;
                            Iterator var33 = var30.iterator();

                            while (var33.hasNext()) {
                                String line2 = (String) var33.next();
                                if (requiredAdjustment < 0) {
                                    w.print(var32, line2);
                                    w.print("  ");
                                    ++requiredAdjustment;
                                    --globalOffset;
                                } else {
                                    w.println(var32, line2);
                                }
                            }

                            String var34 = (String) lines.next();
                            ++numLinesRead;
                            if (requiredAdjustment < 0) {
                                w.print(nextTarget, var34);
                                w.print("  ");
                                --globalOffset;
                            } else {
                                w.println(nextTarget, var34);
                            }
                            break;
                        }
                    } else {
                        while (true) {
                            while (true) {
                                if (numLinesRead >= nextActual) {
                                    continue label238;
                                }

                                String line1 = (String) lines.next();
                                ++numLinesRead;
                                boolean isLast = numLinesRead >= nextActual;
                                int lineNoToPrint = isLast ? nextTarget : -1;
                                if (requiredAdjustment > 0 && x2) {
                                    do {
                                        w.println("");
                                        --requiredAdjustment;
                                        ++globalOffset;
                                    } while (isLast && requiredAdjustment > 0);

                                    w.println(lineNoToPrint, line1);
                                } else {
                                    w.println(lineNoToPrint, line1);
                                }
                            }
                        }
                    }
                }

                while (lines.hasNext()) {
                    String var29 = (String) lines.next();
                    w.println(var29);
                }
                break;
            }
        } catch (Throwable var27) {
            throw var27;
        }

        this._file.delete();
        tempFile.renameTo(this._file);
    }

    public enum LineNumberOption {
        LEADING_COMMENTS, STRETCHED;

        LineNumberOption() {
        }
    }
}
