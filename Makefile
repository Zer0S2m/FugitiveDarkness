run:
	@mvn clean package
	@mvn exec:java -pl fugitive-darkness-api
