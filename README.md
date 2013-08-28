# cornerstone

A re-usable foundation for Clojure projects containing defaults management.

## Usage

#### File driven
The simplest use case is file-driven defaults, where edn configuration is stored at `resources/config/dev.edn` as follows:

    {:foo "bar"}

This can then be read in the application as follows:

    (use 'cornerstone.config)
	(bootstrap {:name "dev"})
	(config :foo) ;; bar

Setting another location for the configuration files can be done with the `:config-dir` parameter in `bootstrap`:

    (bootstrap {:name "dev" :config-dir "/path/to/my/config"})

#### Environment overrides
Sometimes we'll want to override what's in the file with environment variables, as used for example by Heroku.
Anything defined in the file will be overridden by an identically named environment variable.

For example, if your edn configuration is as follows:

    {:foo "bar"}

And the environment variable is set as `foo=baz` then the following code will yield `baz`:

    (use 'cornerstone.config)
	(bootstrap {:name "dev"})
	(config :foo) ;; baz

#### Environment variables only
If you want to drive configuration purely by environment variables that can be achieved by providing a prefix:

    myapp-foo=bar

    (use 'cornerstone.config)
	(bootstrap {:name "dev" :env-prefix "myapp"})
	(config :foo) ;; bar

## License

Copyright © 2013 oliyh

Distributed under the Eclipse Public License, the same as Clojure.
