(ns cornerstone.config-test
  (:use clojure.test
        cornerstone.config))

(deftest has-a-name
  (let [config (bootstrap {:name "dev"})]
    (testing "the config map has the name used to bootstrap it"
      (is (= "dev" (:name config))))))

(deftest reads-from-file
  (let [config (bootstrap {:name "dev" :config-dir "resources/test/"})]
    (testing "config is read from the edn file"
      (is (= "bar" (:foo config)))
      (is (= true (:mytest-toggle config))))))

(deftest reads-from-env-using-prefix
  (let [config (bootstrap {:name "dev" :env-prefix "mytest-" :env {"mytest-foo" "bar"}})]
    (testing "config is read from the env vars when they start with the given prefix"
      (is (= "bar" (:foo config))))))

(deftest env-overrides-file
  (let [config (bootstrap {:name "dev"
                           :config-dir "resources/test/"
                           :env {"foo" "baz"
                                 "mytest-toggle" "false"}})]
    (testing "config in the env overrides what is in the file"
      (is (= "baz" (:foo config)))
      (is (= false (:mytest-toggle config))))))
