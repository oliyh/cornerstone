(ns cornerstone.config-test
  (:use clojure.test
        cornerstone.config))

(deftest has-a-name
  (let [config (bootstrap {:name "dev"})]
    (testing "the config map has the name used to bootstrap it"
      (is (= "dev" (config :name))))))

(deftest empty-if-neither-file-nor-env-vars
  (let [config (bootstrap {:name "dev" :config-dir "non/existent" :env nil})]
    (testing "config is empty"
      (is (= {:name "dev"} (config))))))

(deftest reads-from-file
  (let [config (bootstrap {:name "dev" :config-dir "resources/test/"})]
    (testing "config is read from the edn file"
      (is (= "bar" (config :foo)))
      (is (= true (config :mytest-toggle))))))

(deftest reads-from-env-using-prefix
  (let [config (bootstrap {:name "dev" :env-prefix "mytest-" :env {"mytest-foo" "bar"}})]
    (testing "config is read from the env vars when they start with the given prefix"
      (is (= "bar" (config :foo))))))

(deftest env-overrides-file
  (let [config (bootstrap {:name "dev"
                           :config-dir "resources/test/"
                           :env {"foo" "baz"
                                 "mytest-toggle" "false"}})]
    (testing "config in the env overrides what is in the file if they have the same name"
      (is (= "baz" (config :foo)))
      (is (= false (config :mytest-toggle))))))
