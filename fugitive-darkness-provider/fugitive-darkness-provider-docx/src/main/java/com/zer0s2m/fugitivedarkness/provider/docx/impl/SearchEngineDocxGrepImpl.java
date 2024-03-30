package com.zer0s2m.fugitivedarkness.provider.docx.impl;

import com.zer0s2m.fugitivedarkness.provider.docx.*;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;

/**
 * A search engine for docx files.
 */
class SearchEngineDocxGrepImpl extends SearchEngineDocxGrepAbstract {

    private final HashMap<Integer, ContainerInfoFileContent> contentHashMap = new HashMap<>();

    private final List<ContainerInfoFileContent> foundedMatchers = new ArrayList<>();

    /**
     * Run a search through the entire document. Search criteria:
     * <ul>
     *     <li> Match according to the established pattern {@link SearchEngineDocxGrep#getPattern()}.</li>
     * </ul>
     *
     * @return The search result.
     */
    @Override
    public ContainerInfoSearchDocxFile callGrep() {
        try (final SearchEngineDocxInteraction searchEngineDocxInteraction =
                     new SearchEngineDocxInteraction(getFile())) {
            searchEngineDocxInteraction.load();

            final String xml = searchEngineDocxInteraction.getLoadedData();
            final Document xmlDocument = loadXMLFromString(xml);
            final XPathExpression xPathExpression = getXPathExpression();
            final NodeList nodeList = (NodeList) xPathExpression.evaluate(xmlDocument, XPathConstants.NODESET);

            collectContainerInfoFileContent(nodeList);
            search();

            return new ContainerInfoSearchDocxFile(
                    getFile().getFileName().toString(),
                    getPattern().pattern(),
                    new ArrayList<>(foundedMatchers));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Document loadXMLFromString(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource inputSource = new InputSource(new StringReader(xml));
        return builder.parse(inputSource);
    }

    private XPathExpression getXPathExpression() throws XPathExpressionException {
        final XPath xPath = XPathFactory.newInstance().newXPath();
        return xPath.compile(XMLDocumentDocx.EXPRESSION_XPATH.value());
    }

    private void collectContainerInfoFileContent(final NodeList nodeList) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            contentHashMap.put(i + 1, new ContainerInfoFileContent(
                    i + 1, nodeList.item(i).getTextContent(), null));
        }
    }

    private void search() {
        contentHashMap.forEach((lineNumber, content) -> {
            final Matcher matcher = getPattern().matcher(content.line());
            final List<ContainerInfoFileContentMatch> containerInfoFileContentMatches = new ArrayList<>();

            while (matcher.find()) {
                containerInfoFileContentMatches.add(new ContainerInfoFileContentMatch(
                        matcher.group(),
                        matcher.start(),
                        matcher.end()));
            }

            if (!containerInfoFileContentMatches.isEmpty()) {
                foundedMatchers.add(new ContainerInfoFileContent(
                        content.lineNumber(),
                        content.line(),
                        containerInfoFileContentMatches
                ));
            }
        });
    }

    @Override
    public void close() {
        super.close();

        contentHashMap.clear();
        foundedMatchers.clear();
    }

}
