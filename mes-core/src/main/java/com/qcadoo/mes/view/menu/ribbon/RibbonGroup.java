/**
 * ********************************************************************
 * Code developed by amazing QCADOO developers team.
 * Copyright � Qcadoo Limited sp. z o.o. (2010)
 * ********************************************************************
 */

package com.qcadoo.mes.view.menu.ribbon;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represents ribbon items group
 */
public final class RibbonGroup {

    private String name;

    private final List<RibbonActionItem> items = new LinkedList<RibbonActionItem>();;

    /**
     * get identifier of this ribbon group
     * 
     * @return identifier of this ribbon group
     */
    public String getName() {
        return name;
    }

    /**
     * set identifier of this ribbon group
     * 
     * @param name
     *            identifier of this ribbon group
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * get items of this group
     * 
     * @return items of this group
     */
    public List<RibbonActionItem> getItems() {
        return items;
    }

    /**
     * add item to this group
     * 
     * @param item
     *            item to add
     */
    public void addItem(final RibbonActionItem item) {
        items.add(item);
    }

    /**
     * generates JSON representation of this ribbon group
     * 
     * @return JSON representation of this ribbon group
     * @throws JSONException
     */
    public JSONObject getAsJson() throws JSONException {
        JSONObject groupObject = new JSONObject();
        groupObject.put("name", name);
        JSONArray itemsArray = new JSONArray();
        for (RibbonActionItem item : items) {
            itemsArray.put(item.getAsJson());
        }
        groupObject.put("items", itemsArray);
        return groupObject;
    }

}
