// Copyright (c) 2001  Dustin Sallings <dustin@spy.net>
//
// $Id: LogEntry.java,v 1.1 2001/06/14 09:36:17 dustin Exp $

package net.spy.geo;

import java.util.*;
import java.util.Date;
import java.sql.*;

import net.spy.db.*;
import net.spy.geo.sp.*;

/**
 * A log entry.
 */
public class LogEntry extends Object {

	private int logId=-1;
	private int pointId=-1;
	private int userId=-1;
	private boolean found=false;
	private String info=null;
	private Date timestamp=null;

	/**
	 * Get an instance of LogEntry.
	 */
	public LogEntry() {
		super();
	}

	/**
	 * Get an instance of LogEntry.
	 */
	private LogEntry(ResultSet rs) throws Exception{
		super();
		initFromResultSet(rs);
	}

	private void initFromResultSet(ResultSet rs) throws Exception {
		logId=rs.getInt("log_id");
		pointId=rs.getInt("point_id");
		userId=rs.getInt("user_id");
		found=rs.getBoolean("found");
		info=rs.getString("info");
		timestamp=rs.getTimestamp("ts");
	}

	/**
	 * Get an Enumeration of log entries for a point ID.
	 */
	public static Enumeration getEntriesForPoint(int point_id)
		throws Exception {

		Vector v=new Vector();
		DBSP dbsp=new GetLogEntries(new GeoConfig());
		dbsp.set("point_id", point_id);
		ResultSet rs=dbsp.executeQuery();
		while(rs.next()) {
			v.addElement(new LogEntry(rs));
		}
		rs.close();
		dbsp.close();

		return(v.elements());
	}

	public int getLogId() {
		return(logId);
	}

	public int getPointId() {
		return(pointId);
	}

	public int getUserId() {
		return(userId);
	}

	public boolean getFound() {
		return(found);
	}

	public String getInfo() {
		return(info);
	}

	public void setUserId(int userId) {
		this.userId=userId;
	}

	public void setFound(boolean found) {
		this.found=found;
	}

	public void setInfo(String info) {
		this.info=info;
	}

	public void setPointId(int pointId) {
		this.pointId=pointId;
	}

	public Date getTimestamp() {
		return(timestamp);
	}

	public void save() throws Exception {
		DBSP dbsp=new SaveLogEntry(new GeoConfig());
		dbsp.set("point_id", pointId);
		dbsp.set("user_id", userId);
		dbsp.set("found", found);
		dbsp.set("info", info);
		dbsp.executeUpdate();
		dbsp.close();
	}

}
