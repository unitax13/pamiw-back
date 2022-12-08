package com.example.restapijwttutorial.utils;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;

public class Utils {

   public static String parseMarkdown(String content) {
      MutableDataSet options = new MutableDataSet();
      Parser parser = Parser.builder(options).build();
      HtmlRenderer renderer = HtmlRenderer.builder(options).build();
      Node document = parser.parse(content);
      String outputHtml = renderer.render(document);

      return outputHtml;
   }

}
