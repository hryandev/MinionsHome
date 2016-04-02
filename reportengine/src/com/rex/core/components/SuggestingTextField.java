package com.rex.core.components;

import java.util.List;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnHeaderMode;
import com.vaadin.ui.VerticalLayout;

public class SuggestingTextField extends VerticalLayout{
	private static final long serialVersionUID = 5638650484075031117L;
	
	private VerticalLayout popUpLayout;
	// autosuggestfld, field where listener triggers the popup to be shown
	private CustomTextField autoSuggestFld;
	// resultstable that shows the filtered values
	private Table resultsTable;
	// filterfld used to filter search request
	private CustomTextField filterFld;
	// popupview
	private PopupView popupView;
	private boolean isSearchOn = true;
	private boolean tableRowSelected = false;
	private List<CountryBean> resultset;
	private String inputValue;
	private AutoSuggestResultsetBuilder autoSuggestResultsetBuilder;
	final DatabaseAccessService databaseAccessService = new DatabaseAccessServiceImpl();
	private boolean valueChanged = false;

	public SuggestingTextField() {
		initAutoSuggestFld();
		initTable();
		initFilterFld();
		initPopUpLayout();
		initPopUpView();
		registerListeners();

		addComponent(autoSuggestFld);
		addComponent(popupView);
	}

	private void registerListeners() {
		registerAutoSuggestFldListeners();
		registerResultsTableListeners();
		registerPopUpViewListeners();
		registerFilterFldListeners();
	}

	private boolean filterFldListening = true;

	private void registerFilterFldListeners() {
		/*filterFld.addListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (valueChanged == true && filterFldListening) {
					inputValue = (String) event.getProperty().getValue();
					resultset = autoSuggestResultsetBuilder.getResultset();
					setResultsTableValues();
					valueChanged = false;
				}
			}
		});*/
		
		/*filterFld.addListener(new TextChangeListener() {

			@Override
			public void textChange(TextChangeEvent event) {
				if (filterFldListening) {
					inputValue = event.getText();
					resultset = autoSuggestResultsetBuilder.getResultset();
					setResultsTableValues();
				}
			}
		});*/
		
		filterFld.addValueChangeListener(e -> {
			if (valueChanged == true && filterFldListening) {
				inputValue = (String) ((ValueChangeEvent) e).getProperty().getValue();
				resultset = databaseAccessService.filterCountryTableInDatabase(inputValue);
				setResultsTableValues();
				valueChanged = false;
			}
		});
		
		filterFld.addTextChangeListener(e -> {
			if (filterFldListening) {
				inputValue = e.getText();
				resultset = databaseAccessService.filterCountryTableInDatabase(inputValue);
				setResultsTableValues();
			}
		});
		
	}

	public interface AutoSuggestResultsetBuilder {
		public List<AutoSuggestResultBean> getResultset();
	}

	public void setResultsTableValues(
			AutoSuggestResultsetBuilder autoSuggestResultsetBuilder) {
		this.autoSuggestResultsetBuilder = autoSuggestResultsetBuilder;
	}

	private void registerPopUpViewListeners() {
		/*popupView.addListener(new PopupVisibilityListener() {
			public void popupVisibilityChange(PopupVisibilityEvent event) {
				if (!event.isPopupVisible()) {
					if (!tableRowSelected) {
						autoSuggestListening = false;
						autoSuggestFld.setValue("");
						autoSuggestListening = true;
					}
					tableRowSelected = false;
					filterFldListening = false;
					filterFld.setValue("");
					filterFldListening = true;
				}
			}
		});*/
		
		popupView.addPopupVisibilityListener(e -> {
			if (!e.isPopupVisible()) {
				if (!tableRowSelected) {
					autoSuggestListening = false;
					autoSuggestFld.setValue("");
					autoSuggestListening = true;
				}
				tableRowSelected = false;
				filterFldListening = false;
				filterFld.setValue("");
				filterFldListening = true;
			}
		});
	}

	private void registerResultsTableListeners() {
		resultsTable.addValueChangeListener(e -> {
			Object value = resultsTable.getValue();
			if (value != null) {
				autoSuggestListening = false;
				autoSuggestFld.setValue(resultsTable
						.getContainerProperty(value, "value").getValue()
						.toString());
				autoSuggestListening = true;
				tableRowSelected = true;
				popupView.setPopupVisible(false);
				filterFldListening = false;
				filterFld.setValue("");
				filterFldListening = true;
			}
		});
		
		/*resultsTable.addListener(new ValueChangeListener() {
			public void valueChange(ValueChangeEvent event) {
				Object value = resultsTable.getValue();
				if (value != null) {
					autoSuggestListening = false;
					autoSuggestFld.setValue(resultsTable
							.getContainerProperty(value, "value").getValue()
							.toString());
					autoSuggestListening = true;
					tableRowSelected = true;
					popupView.setPopupVisible(false);
					filterFldListening = false;
					filterFld.setValue("");
					filterFldListening = true;
				}
			}
		});*/
	}

	private boolean autoSuggestListening = true;

	private void registerAutoSuggestFldListeners() {
		autoSuggestFld.addTextChangeListener(e -> {
			if (e.getText().length() > 0 && isSearchOn
					&& autoSuggestListening) {
				popupView.setPopupVisible(true);
				valueChanged = true;
				filterFld.setValue(e.getText());
				filterFld.focus();
				filterFld.setCursorPosition(autoSuggestFld
						.getCursorPosition());
			}
		});
		
		/*autoSuggestFld.addListener(new TextChangeListener() {
			public void textChange(TextChangeEvent event) {
				if (event.getText().length() > 0 && isSearchOn
						&& autoSuggestListening) {
					popupView.setPopupVisible(true);
					valueChanged = true;
					filterFld.setValue(event.getText());
					filterFld.focus();
					filterFld.setCursorPosition(autoSuggestFld
							.getCursorPosition());
				}
			}
		});*/
	}

	private void initTable() {
		resultsTable = new Table();
		resultsTable.setSelectable(false);
		resultsTable.addContainerProperty("id", Long.class, null);
		resultsTable.addContainerProperty("value", String.class, null);
		resultsTable.setWidth("200px");
		resultsTable.setImmediate(true);
		resultsTable.setColumnHeaderMode(ColumnHeaderMode.HIDDEN);
		resultsTable.setVisibleColumns(new Object[] {"value"});
		
		
		resultsTable.addValueChangeListener(e -> {
			
		});
		
	}

	public void setResultsTableValues(List<CountryBean> values) {
		resultsTable.removeAllItems();
		for (CountryBean bean : values) {
			Object item = resultsTable.addItem();
			resultsTable.getContainerProperty(item, "id")
					.setValue(bean.getId());
			resultsTable.getContainerProperty(item, "value").setValue(
					bean.getName());
		}
	}

	public void setResultsTableValues() {
		resultsTable.removeAllItems();
		
		for (CountryBean bean : resultset) {
			Object item = resultsTable.addItem();
			resultsTable.getContainerProperty(item, "id")
					.setValue(bean.getId());
			resultsTable.getContainerProperty(item, "value").setValue(
					bean.getName());
		}
		
	}

	private void initAutoSuggestFld() {
		autoSuggestFld = new CustomTextField();
		autoSuggestFld.setWidth("200px");
		autoSuggestFld.setImmediate(true);
	}

	private void initFilterFld() {
		filterFld = new CustomTextField();
		filterFld.setWidth("200px");
		filterFld.setImmediate(true);
	}

	private void initPopUpLayout() {
		popUpLayout = new VerticalLayout();
		popUpLayout.setWidth("200px");
		popUpLayout.addComponent(filterFld);
		popUpLayout.addComponent(resultsTable);
	}

	private void initPopUpView() {
		popupView = new PopupView(null, popUpLayout);
		popupView.setHideOnMouseOut(false);
	}

	public VerticalLayout getPopUpLayout() {
		return popUpLayout;
	}

	public CustomTextField getAutoSuggestFld() {
		return autoSuggestFld;
	}

	public Table getResultsTable() {
		return resultsTable;
	}

	public CustomTextField getFilterFld() {
		return filterFld;
	}

	public PopupView getPopupView() {
		return popupView;
	}

	public boolean isSearchOn() {
		return isSearchOn;
	}

	public void setSearchOn(boolean isSearchOn) {
		this.isSearchOn = isSearchOn;
	}

	public void setMaxLength(int maxLength) {
		this.autoSuggestFld.setMaxLength(maxLength);
		this.filterFld.setMaxLength(maxLength);
	}

	public String getInputValue() {
		return inputValue;
	}
}
