
ifeq (run,$(firstword $(MAKECMDGOALS)))
  # use the rest as arguments for "run"
  RUN_ARGS := $(wordlist 2,$(words $(MAKECMDGOALS)),$(MAKECMDGOALS))
  # ...and turn them into do-nothing targets
  $(eval $(RUN_ARGS):;@:)
endif

default: build

build:
	@find . -name "*.java" -print0 | xargs -0 javac -g -d dist
	@cp src/aoc.sh dist/
	@chmod u+x dist/aoc.sh

