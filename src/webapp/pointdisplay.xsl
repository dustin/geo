<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="html"/>

<xsl:template match="geocaches">
	<table border="1">
		<xsl:if test="origin">
			<tr>
				<td colspan="12">
					Origin point:
					<xsl:value-of select="origin/longitude"/>&#160;
					<xsl:value-of select="origin/latitude"/>
				</td>
			</tr>
		</xsl:if>
		<tr>
			<th>Name</th>
			<th>Waypoint</th>
			<th>Longitude</th>
			<th>Latitude</th>
			<th>Country</th>
			<xsl:if test="origin">
				<th>Distance</th>
				<th>Direction</th>
			</xsl:if>
			<th>Description</th>
			<th>Created</th>
			<th>Difficulty</th>
			<th>Terrain</th>
			<th>Creator</th>
		</tr>
		<xsl:apply-templates select="point"/>
	</table>
</xsl:template>

<xsl:template match="point">
	<tr>
		<td>
			<a href="showpoint.jsp?point={pointid}">
				<xsl:value-of select="name"/></a>
		</td>
		<td><xsl:value-of select="waypoint"/></td>
		<td><xsl:value-of select="longitude"/></td>
		<td><xsl:value-of select="latitude"/></td>
		<td><xsl:value-of select="country"/></td>
		<xsl:if test="../origin">
			<td><xsl:value-of select="distance"/></td>
			<td><xsl:value-of select="bearing"/>
				(<xsl:value-of select="direction"/>)</td>
		</xsl:if>
		<td><xsl:value-of select="description"/></td>
		<td><xsl:value-of select="created"/></td>
		<td><xsl:value-of select="difficulty"/></td>
		<td><xsl:value-of select="terrain"/></td>
		<td><xsl:value-of select="creatorid"/></td>
	</tr>
</xsl:template>

</xsl:stylesheet>
