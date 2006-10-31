// Copyright (c) 2001  Dustin Sallings <dustin@spy.net>
//
// $Id: LogEntry.java,v 1.1 2001/06/14 09:36:17 dustin Exp $

package net.spy.geo;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import net.spy.db.DBSPLike;
import net.spy.geo.sp.GetLogEntries;
import net.spy.geo.sp.SaveLogEntry;
import net.spy.util.CloseUtil;

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
	public static Collection<LogEntry> getEntriesForPoint(int point_id)
		throws Exception {

		Collection<LogEntry> rv=new ArrayList<LogEntry>();
		GetLogEntries db=new GetLogEntries(GeoConfig.getInstance());
		try {
			db.setPointId(point_id);
			ResultSet rs=db.executeQuery();
			while(rs.next()) {
				rv.add(new LogEntry(rs));
			}
			rs.close();
		} finally {
			CloseUtil.close((DBSPLike)db);
		}

		return rv;
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

	public void setUserId(int to) {
		this.userId=to;
	}

	public void setFound(boolean to) {
		this.found=to;
	}

	public void setInfo(String to) {
		this.info=to;
	}

	public void setPointId(int to) {
		this.pointId=to;
	}

	public Date getTimestamp() {
		return(timestamp);
	}

	public void save() throws Exception {
		SaveLogEntry db=new SaveLogEntry(GeoConfig.getInstance());
		db.setPointId(pointId);
		db.setUserId(userId);
		db.setFound(found);
		db.setInfo(info);
		db.executeUpdate();
		db.close();
	}

}
