
ifeq (run,$(firstword $(MAKECMDGOALS)))
  # use the rest as arguments for "run"
  RUN_ARGS := $(wordlist 2,$(words $(MAKECMDGOALS)),$(MAKECMDGOALS))
  # ...and turn them into do-nothing targets
  $(eval $(RUN_ARGS):;@:)
endif

default: build

build:
	find . -path ./test -prune -o -name "*.java" -print0 | xargs -0 javac -cp lib/lombok.jar -g -d dist
	@cp src/aoc.sh dist/
	@cp -R lib/ dist
	@chmod u+x dist/aoc.sh

