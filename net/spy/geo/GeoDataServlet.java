// Copyright (c) 2001  Dustin Sallings <dustin@spy.net>
//
// $Id: GeoDataServlet.java,v 1.4 2001/06/14 21:21:18 dustin Exp $

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
			xmlList(os, p, e);
		} catch(Exception ex) {
			throw new ServletException("Error sending XML" + ex, ex);
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
			el.appendChild(d.createTextNode("" + p.getLongitude()));
			root.appendChild(el);

			el=d.createElement("longitude");
			el.appendChild(d.createTextNode("" + p.getLatitude()));
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

	private void sendDoc(OutputStream os, Document d) throws Exception {
			OutputFormat format=new OutputFormat(d);
			format.setOmitXMLDeclaration(true);
			format.setIndenting(true);
			XMLSerializer serial = new XMLSerializer(os, format);
			serial.asDOMSerializer();
			serial.serialize(d.getDocumentElement());
	}

}
