docker_compose_bin := $(shell command -v docker-compose 2> /dev/null)
mvn_bin := $(shell command -v mvn 2> /dev/null)

define run-docker-dev
$(docker_compose_bin) -f docker-compose.dev.yml up -d --force-recreate
endef

define run-migrate
cd ./fugitive-darkness-models && \
 	$(mvn_bin) clean install && \
	$(mvn_bin) flyway:migrate \
		-Dflyway.user=$(FD_POSTGRES_USER) \
		-Dflyway.password=$(FD_POSTGRES_PASSWORD) \
		-Dflyway.schemas=public \
		-Dflyway.url=jdbc:postgresql://$(FD_POSTGRES_HOST):$(FD_POSTGRES_PORT)/$(FD_POSTGRES_DB)
endef

run:
	@$(mvn_bin) clean install package
	@$(mvn_bin) exec:java -pl fugitive-darkness-api

run-migrate:
	@$(run-migrate)

run-docker:
	@$(run-docker-dev)

dev:
	@$(run-docker-dev)
	@$(run-migrate)
	@$(mvn_bin) clean install package
	@$(mvn_bin) exec:java -pl fugitive-darkness-api

run-debug:
	java \
		-XX:NativeMemoryTracking=detail \
		-XX:StartFlightRecording:+jdk.VirtualThreadStart#enabled=true,\+jdk.VirtualThreadEnd#enabled=true,filename=./out/recording.jfr \
		-jar ./fugitive-darkness-api/target/fugitive-darkness-api-0.0.5-fat.jar

show-debug:
	@jfr view hot-methods ./out/recording.jfr
	@jfr view allocation-by-site ./out/recording.jfr
	@echo ""
	@echo "Event Types by Name (Experimental)"
	@echo ""
	@echo "Event Type                                                                Count"
	@echo "------------------------------------------------------------------------ ------"
	@jfr view events-by-name ./out/recording.jfr | grep "Virtual Thread"
	@jfr view native-memory-committed ./out/recording.jfr
