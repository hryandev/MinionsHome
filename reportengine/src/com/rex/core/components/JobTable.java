package com.rex.core.components;

public class JobTable extends CustomTable {
	private static final long serialVersionUID = -8421993328842328318L;

	
	public JobTable() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	public JobTable(String caption) {
		super(caption);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void openCustomWindow(){
		JobListWindow jw = new JobListWindow(this);
    	getUI().addWindow(jw);
    	jw.center();
        jw.focus();
    }

}

