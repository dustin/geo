// Copyright (c) 2001  Dustin Sallings <dustin@spy.net>
//
// $Id: GeoDataServlet.java,v 1.5 2001/06/15 10:23:11 dustin Exp $

package net.spy.geo;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.xml.sax.*;
import org.w3c.dom.*;

import org.apache.xerces.dom.DocumentImpl;
import org.apache.xml.serialize.*;

/**
 * A servlet for geo data.
 */
public class GeoDataServlet extends HttpServlet {

	private static final String DTD=
		"http://bleu.west.spy.net/~dustin/dtd/geocaches.dtd";

	// The list.
	private CachePointList list=null;

	private static final int FORMAT_XML=0;
	private static final int FORMAT_TAB=1;

	/**
	 * Initialize it.
	 */
	public void init(ServletConfig conf) throws ServletException {
		super.init(conf);
		try {
			log("Initializing CachePointList");
			list=new CachePointList();
		} catch(Exception e) {
			throw new ServletException("Error making CachePointList", e);
		}
	}

	/**
	 * Tell the point list to close.
	 */
	public void destroy() {
		log("Destroying CachePointList");
		// Tell it we're done.
		list.finish();
		// This will cause it to jump out of its loop.
		list.requestUpdate();
	}

	/**
	 * Process GET requests.
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException {
		process(req, res);
	}

	/**
	 * Process POST requests.
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException {
		process(req, res);
	}

	/**
	 * Process request.
	 */
	public void process(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException {

		String lon_s=req.getParameter("long");
		if(lon_s != null && lon_s.length()==0) {
			lon_s=null;
		}
		String lat_s=req.getParameter("lat");
		if(lat_s != null && lat_s.length()==0) {
			lat_s=null;
		}
		String max_s=req.getParameter("max");
		if(max_s != null && max_s.length()==0) {
			max_s=null;
		}
		String zip_s=req.getParameter("zip");
		if(zip_s != null && zip_s.length()==0) {
			zip_s=null;
		}

		int format=FORMAT_XML;
		String format_s =req.getParameter("format");
		if(format_s!=null && format_s.equals("tab")) {
			format=FORMAT_TAB;
		}

		log("Requested format:  " + format_s + "(" + format +")");

		Point p=null;

		if(lon_s != null && lat_s != null) {
			float lon=Float.parseFloat(lon_s);
			float lat=Float.parseFloat(lat_s);
			p=new Point(lat, lon);
		} else if(zip_s!=null) {
			try {
				p=Point.getPointByZip(Integer.parseInt(zip_s));
			} catch(Exception e) {
				throw new ServletException(
					"Error looking up coords for zip " + zip_s, e);
			}
		}

		Enumeration e=null;
		if(max_s!=null && p!=null) {
			e=list.getPoints(p, Double.parseDouble(max_s));
		} else {
			if(p!=null) {
				e=list.getPoints(p);
			} else {
				e=list.getPoints();
			}
		}

		ServletOutputStream os=res.getOutputStream();
		try {
			res.setContentType("text/plain");
			switch(format) {
				case FORMAT_XML:
					xmlList(os, p, e);
					break;
				case FORMAT_TAB:
					tabList(os, p, e);
					break;
			}
		} catch(Exception ex) {
			throw new ServletException("Error sending report" + ex, ex);
		}
		os.close();
	}

	// Send out the XML.
	private void xmlList(ServletOutputStream os, Point p, Enumeration en)
		throws Exception {

		os.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

		os.println(("<!DOCTYPE geocaches SYSTEM \"" +DTD+ "\">\n"));

		os.println("<geocaches>\n");

		if(p!=null) {
			Document d=new DocumentImpl();
			Element root=d.createElement("origin");
			d.appendChild(root);

			Element el=d.createElement("latitude");
			el.appendChild(d.createTextNode("" + p.getLatitude()));
			root.appendChild(el);

			el=d.createElement("longitude");
			el.appendChild(d.createTextNode("" + p.getLongitude()));
			root.appendChild(el);

			sendDoc(os, d);
		}

		for(; en.hasMoreElements(); ) {
			CachePoint cp=(CachePoint)en.nextElement();
			Document d=new DocumentImpl();

			Element e=cp.appendXML(d);
			if(p!=null) {
				GeoVector v=p.diff(cp);
				Element el=d.createElement("distance");
				el.appendChild(d.createTextNode("" + v.getDistance()));
				e.appendChild(el);
				el=d.createElement("bearing");
				el.appendChild(d.createTextNode("" + v.getBearing()));
				e.appendChild(el);
				el=d.createElement("direction");
				el.appendChild(d.createTextNode(v.getDirection()));
				e.appendChild(el);
			}

			sendDoc(os, d);

		}

		os.println("\n</geocaches>");
	}

	// Tab delimited version
	private void tabList(ServletOutputStream os, Point p, Enumeration en)
		throws Exception {

		os.println("# Tab delimited point list, generated on " + new Date());
		os.println("# Fields are as follows:");
		os.print("# latitude\tlongitude\tname\tplacement date\turl");
		if(p!=null) {
			os.println("\tdistance\tbearing\tdirection");
		} else {
			os.println("");
		}
		os.println("# Distances are in miles");
		os.println("# lat and long are decimal with negative being south ");
		os.println("# and west respectively ");

		for(; en.hasMoreElements(); ) {
			CachePoint cp=(CachePoint)en.nextElement();
			os.print(cp.getLatitude());
			os.print("\t" + cp.getLongitude());
			os.print("\t" + cp.getName());
			os.print("\t" + cp.getDateCreated());
			os.print(
				"\thttp://bleu.west.spy.net/~dustin/geo/showpoint.jsp?point="
				+ cp.getPointId());
			if(p!=null) {
				GeoVector gv=p.diff(cp);
				os.print("\t" + gv.getDistance());
				os.print("\t" + gv.getBearing());
				os.print("\t" + gv.getDirection());
			}
			os.println("");
		}
	}

	private void sendDoc(OutputStream os, Document d) throws Exception {
			OutputFormat format=new OutputFormat(d);
			format.setOmitXMLDeclaration(true);
			format.setIndenting(true);
			XMLSerializer serial = new XMLSerializer(os, format);
			serial.asDOMSerializer();
			serial.serialize(d.getDocumentElement());
	}

}
