package client.services;

import org.commonmark.Extension;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.List;

public class MarkdownService {
    private final Parser parser;
    private final HtmlRenderer renderer;

    /**
     * This is the markdownService class to split logic from HomePageCtrl
     */
    public MarkdownService() {
        List<Extension> extensions = List.of(TablesExtension.create(), AutolinkExtension.create());
        this.parser = Parser.builder().extensions(extensions).build();
        this.renderer = HtmlRenderer.builder().extensions(extensions).build();
    }

    /**
     *
     * @param markdownText String that represents the markdowntext
     * @return returns convertion to html
     */
    public String convertToHtml(String markdownText) {
        try {
            Node document = parser.parse(markdownText);
            return renderer.render(document);
        } catch (Exception e) {
            return "<html><body><h2>Error</h2><p>" + e.getMessage() + "</p></body></html>";
        }
    }
}
