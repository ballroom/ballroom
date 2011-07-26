About Ballroom
==============

Ballroom is a shared widget library for JBoss projects that build on GWT.
Prominent samples are:

* Drools Guvnor 
* JBossAS 7 
* Riftsaw 
* JBPM 5
* Savarra

Participating in Ballroom
-------------------------

* WIKI: http://community.jboss.org/wiki/BallroomLookFeel
* Mailing List: https://lists.jboss.org/mailman/listinfo/jboss-as7-dev
* IRC: irc://irc.freenode.org/jboss-as7

Running the showcase
--------------------
The showcase should give an idea what widgets exists and what functionality they offer.
It's work in progress and subject to continuous improvements. But in general the widget set
should be kept small and straight forward. 

Running the showcase is simple:

	cd showcase
	mvn clean gwt:run
	

Using ballroom in your project
------------------------------

In order to use ballroom you need to import the GWT module descriptor:

	<module>

		<inherits name="org.jboss.as.console.Framework"/>
	
		[...]
		
		<replace-with class="org.jboss.as.console.spi.NoopFramework">
	        <when-type-is class="org.jboss.as.console.client.spi.Framework"/>
	    </replace-with>
		
	</module>
	
Ballroom ships with two different styles. One for jboss.org projects and another one for branded RH products:


	* <inherits name="org.jboss.as.console.Framework"/>
	* <inherits name="org.jboss.as.console.Framework_RH"/>
	
Library dependencies
--------------------

We tried to keep the dependencies to a minimum, but some framework dependencies need to be provided
if you want to leverage all provided widgets. Apart from GWT > 2.2, ballroom depends on these API's:

* GWT Event Bus
* AutoBean Factory
* GWTP PlaceManager 
	
These framework dependencies are provided through an simple SPI interface:

	public interface Framework {

	    EventBus getEventBus();
	    PlaceManager getPlaceManager();
	    AutoBeanFactory getBeanFactory();
	}
	
A specific implementation is declared through the module descriptor:

	<module>
		
		[...]
		
		<replace-with class="org.jboss.as.console.client.spi.NoopFramework">
	        <when-type-is class="org.jboss.as.console.client.spi.Framework"/>
	    </replace-with>
		
		[...]
		
	</module>
	
	
Releasing ballroom
------------------

Please make sure to run the 'release' profile, when doing a relase.
And remember: No snapshots allowed!



