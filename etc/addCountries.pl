#!/usr/local/bin/perl

while(<>) {
	chomp; chomp;
	@a=split(/\t/, $_, 2);
	$a[1]=~s/\'/\'\'/g;
	print "insert into geo_countries(abbr, name) values('"
		. $a[0] . "', '" . $a[1] . "');\n";
}
