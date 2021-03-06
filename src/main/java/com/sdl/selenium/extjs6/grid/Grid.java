package com.sdl.selenium.extjs6.grid;

import com.sdl.selenium.WebLocatorUtils;
import com.sdl.selenium.web.SearchType;
import com.sdl.selenium.web.WebLocator;
import com.sdl.selenium.web.table.Table;
import com.sdl.selenium.web.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

public class Grid extends Table {
    private static final Logger LOGGER = LoggerFactory.getLogger(Grid.class);

    public Grid() {
        setClassName("Grid");
        setBaseCls("x-grid");
        setTag("*");
        WebLocator header = new WebLocator().setClasses("x-title").setRoot("//");
        setTemplateTitle(new WebLocator(header));
    }

    public Grid(WebLocator container) {
        this();
        setContainer(container);
    }

    /**
     * <pre>{@code
     * Grid grid = new Grid().setHeaders("Company", "Price", "Change");
     * }</pre>
     *
     * @param headers grid's headers in order
     * @param <T>     element which extended the Grid
     * @return this Grid
     */
    public <T extends Table> T setHeaders(final String... headers) {
        List<WebLocator> list = new ArrayList<>();
        for (int i = 0; i < headers.length; i++) {
            WebLocator headerEL = new WebLocator(this).setTag("*[" + (i + 1) + "]").setClasses("x-column-header").
                    setText(headers[i], SearchType.DEEP_CHILD_NODE_OR_SELF, SearchType.EQUALS);
            list.add(headerEL);
        }
        setChildNodes(list.toArray(new WebLocator[list.size()]));
        return (T) this;
    }

    @Override
    public Row getRow(int rowIndex) {
        return new Row(this, rowIndex).setInfoMessage("-Row");
    }

    @Override
    public Row getRow(String searchElement) {
        return new Row(this, searchElement, SearchType.EQUALS).setInfoMessage("-Row");
    }

    @Override
    public Row getRow(String searchElement, SearchType... searchTypes) {
        return new Row(this, searchElement, searchTypes).setInfoMessage("-Row");
    }

    public Row getRow(Cell... byCells) {
        return new Row(this, byCells).setInfoMessage("-Row");
    }

    public Row getRow(int indexRow, Cell... byCells) {
        return new Row(this, indexRow, byCells).setInfoMessage("-Row");
    }

    @Override
    public Cell getCell(int rowIndex, int columnIndex) {
        Row row = getRow(rowIndex);
        return new Cell(row, columnIndex).setInfoMessage("cell - Table");
    }

    @Override
    public Cell getCell(String searchElement, SearchType... searchTypes) {
        Row row = new Row(this);
        return new Cell(row).setText(searchElement, searchTypes);
    }

    public Cell getCell(int rowIndex, int columnIndex, String text) {
        Row row = getRow(rowIndex);
        return new Cell(row, columnIndex, text, SearchType.EQUALS);
    }

    public Cell getCell(String searchElement, String columnText, SearchType... searchTypes) {
        Row row = getRow(searchElement, SearchType.CONTAINS);
        return new Cell(row).setText(columnText, searchTypes);
    }

    @Override
    public Cell getCell(String searchElement, int columnIndex, SearchType... searchTypes) {
        return new Cell(new Row(this, searchElement, searchTypes), columnIndex);
    }

    public Cell getCell(int columnIndex, Cell... byCells) {
        return new Cell(getRow(byCells), columnIndex);
    }

    public Cell getCell(int columnIndex, String text, Cell... byCells) {
        return new Cell(getRow(byCells), columnIndex, text, SearchType.EQUALS);
    }

    public boolean waitToActivate(int seconds) {
        String info = toString();
        int count = 0;
        boolean hasMask;
        while ((hasMask = hasMask()) && (count < seconds)) {
            count++;
            LOGGER.info("waitToActivate:" + (seconds - count) + " seconds; " + info);
            Utils.sleep(900);
        }
        return !hasMask;
    }

    private boolean hasMask() {
        WebLocator mask = new WebLocator(this).setClasses("x-mask").setElPathSuffix("style", "not(contains(@style, 'display: none'))").setAttribute("aria-hidden", "false").setInfoMessage("Mask");
        return mask.waitToRender(500, false);
    }

    /**
     * Will Fail if id is null
     *
     * @return attribute
     */
    protected String getAttrId() {
        String id = getAttributeId();
        assertThat(MessageFormat.format("{0} id is null. The path is: {1}", getPathBuilder().getClassName(), getXPath()), id, notNullValue());
        return id;
    }

    /**
     * Scroll on the top in Grid
     *
     * @return true if scrolled
     */
    public boolean scrollTop() {
        String id = getAttrId();
        return scrollTop(id);
    }

    protected boolean scrollTop(String id) {
        String script = "return (function(c){var a = c.view.getScrollable()._scrollElement;if(a.dom.scrollTop != 0){a.dom.scrollTop = 0;return true}return false})(window.Ext.getCmp('" + id + "'))";
        return executeScrollScript("scrollTop", script);
    }


    public boolean scrollBottom() {
        String id = getAttrId();
        String script = "return (function(c){var b=c.view.scrollable.getMaxUserPosition().y;c.view.scrollBy(0, b);setTimeout(function(){c.view.scrollBy(0, 1000);},50);return true})(window.Ext.getCmp('" + id + "'))";
        return executeScrollScript("scrollButtom", script);
    }

    /**
     * Scroll Up one visible page in Grid
     *
     * @return true if scrolled
     */

    public boolean scrollPageUp() {
        String id = getAttrId();
        String script = "return (function(c){var a=c.view,b=a.getScrollable()._scrollElement;if(b.dom.scrollTop>0){b.dom.scrollTop-=a.getHeight()-13;return true}return false})(window.Ext.getCmp('" + id + "'))";
        return executeScrollScript("scrollPageUp", script);
    }

    /**
     * Scroll Down one visible page in Grid
     *
     * @return true if scrolled
     */
    public boolean scrollPageDown() {
        String id = getAttrId();
        return scrollPageDown(id);
    }

    protected boolean scrollPageDown(String id) {
        String script = "return (function(c){var a=c.view,b=a.getScrollable()._scrollElement;if(b.dom.scrollTop<a.scrollable.getMaxPosition().y){b.dom.scrollTop+=a.getHeight()-13;return true}return false})(window.Ext.getCmp('" + id + "'))";
        return executeScrollScript("scrollPageDown", script);
    }

    protected boolean executeScrollScript(String info, String script) {
        Boolean scrolled = (Boolean) WebLocatorUtils.doExecuteScript(script);
        LOGGER.info(this + " - " + info + " > " + scrolled);
        return scrolled;
    }

    @Override
    public boolean waitToPopulate(int seconds) {
        Row row = getRow(1).setVisibility(true).setRoot("//..//").setInfoMessage("first Row");
        WebLocator body = new WebLocator(this).setClasses("x-grid-header-ct"); // TODO see if must add for all rows
        row.setContainer(body);
        return row.waitToRender(seconds * 1000L);
    }

    public List<String> getHeaders() {
        List<String> headers = new ArrayList<>();
        WebLocator header = new WebLocator(this).setClasses("x-grid-header-ct");
        String headerText = header.getText();
        if (headerText == null || "".equals(headerText)) {
            headerText = header.getText();
        }
        Collections.addAll(headers, headerText.trim().split("\n"));
        return headers;
    }

    @Override
    public List<List<String>> getCellsText() {
        Row rowsEl = new Row(this);
        Row rowEl = new Row(this, 1);
        Cell columnsEl = new Cell(rowEl);
        int rows = rowsEl.size() + 1;
        int columns = columnsEl.size();
        if (rows <= 0) {
            return null;
        } else {
            List<List<String>> listOfList = new ArrayList<>();
            for (int i = 1; i <= rows; ++i) {
                List<String> list = new ArrayList<>();
                for (int j = 1; j <= columns; ++j) {
                    list.add(this.getCell(i, j).getText());
                }
                listOfList.add(list);
            }
            return listOfList;
        }
    }
}
