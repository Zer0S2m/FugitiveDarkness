run:
	@mvn clean install package
	@mvn exec:java -pl fugitive-darkness-api
