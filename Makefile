# Copyright (c) 2000  Dustin Sallings <dustin@spy.net>
# $Id: Makefile,v 1.5 2001/06/13 03:45:24 dustin Exp $

JAVAHOME=/usr
JAR=$(JAVAHOME)/bin/jar
# JAVAC=$(JAVAHOME)/bin/javac
JAVAC=jikes
JAVA=$(JAVAHOME)/bin/java
MYLIB=$(HOME)/lib/java

.SUFFIXES: .java .spt .class .jar

C1=$(MYLIB):$(MYLIB)/jsdk.jar:$(MYLIB)/spy.jar:$(MYLIB)/cos.jar
CLASSPATH=$(C1):/System/Library/Frameworks/JavaVM.framework/Versions/1.3/Classes/classes.jar:.

CLASSES=\
		net/spy/geo/sp/GetPointByZip.class \
		net/spy/geo/sp/GetAllPoints.class \
		net/spy/geo/sp/RegisterUser.class \
		net/spy/geo/sp/LookupUserByName.class \
		net/spy/geo/sp/LookupUserByID.class \
		net/spy/geo/GeoConfig.class \
		net/spy/geo/Point.class \
		net/spy/geo/PointComparator.class \
		net/spy/geo/GeoVector.class \
		net/spy/geo/CachePoint.class \
		net/spy/geo/CachePointList.class \
		net/spy/geo/GeoUser.class \
		net/spy/geo/GeoBean.class

all: $(CLASSES)

jar: all
	$(JAR) cv0f geo.jar $(CLASSES)

release: jar release.tgz

clean:
	rm -f $(CLASSES)

.java.class:
	env CLASSPATH=$(CLASSPATH) $(JAVAC) $<

.spt.java: $<
	env CLASSPATH=$(CLASSPATH) $(JAVA) net.spy.util.SPGen $<
