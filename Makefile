
METADATA_EXTRACTOR_URL=https://github.com/agiledigital/mule-metadata-extractor/releases/download/v1.0.14/mule-metadata-extractor-1.0.14-standalone.jar
METADATA_EXTRACTOR_JAR=mule-metadata-extractor-1.0.14-standalone.jar

CLIENT_FILES := $(shell find src -type f -iname '*.cljs')
CLIENT_PUBLIC_FILES := $(shell find public -type f ! -path "public/img/icons/*")

all: dist/.timestamp
.PHONY: all

dist/.timestamp: node_modules/.installed libs/reagent/target/reagent-0.8.1-BINDFIX.jar $(CLIENT_FILES)
	@echo ">>> Building Client Module (Release)"
	npm run build
	touch $@

node_modules/.installed: package.json libs/reagent/target/reagent-0.8.1-BINDFIX.jar
	@echo ">>> Installing dependencies for Client Module"
	npm install && touch node_modules/.installed

libs/reagent/target/reagent-0.8.1-BINDFIX.jar: libs/reagent/project.clj
	@echo ">>> Installing forked version of Reagent into local repo"
	cd libs/reagent && lein install

libs/reagent/.timestamp: .gitmodules
	@echo ">>> Updating submodule"
	git submodule update --init --recursive --remote
	touch $@

dependencies/$(METADATA_EXTRACTOR_JAR):
	@echo ">>> Downloading mule-metadata-extractor binary"
	mkdir -p dependencies
	curl -L --show-error --fail -o dependencies/$(METADATA_EXTRACTOR_JAR) $(METADATA_EXTRACTOR_URL)

clean:
	rm -rf \
	public/js \
	dist \
	build \
	.shadow-cljs \
	node_modules \
	public/mappings.json \
	public/img/icons \
	libs/reagent/target \
	dependencies

.PHONY: clean
