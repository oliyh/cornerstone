(ns cornerstone.config
  (:use [clojure.java.io :only [as-file]])
  (:require [clojure.string :as string]
            [clojure.edn :as edn]))

(def config-atom (atom {}))

(defn- read-config-file
  "Loads config from resources/config/<env>.edn"
  [config-dir config-name]
  (let [file-name (str config-dir config-name ".edn")]
    (when (.exists (as-file file-name))
      (edn/read-string (slurp file-name)))))

(defn- read-env-vars [overridable env-prefix env-vars]
  (reduce
   (fn [env [k v]]
     (if-let [env-key (cond
                       (and env-prefix (.startsWith k env-prefix)) (keyword (string/replace-first k env-prefix ""))
                       (contains? overridable (keyword k)) (keyword k)
                       :else nil)]
       (assoc env env-key (if (symbol? (edn/read-string v)) v (edn/read-string v)))
       env))
   {} env-vars))

(defn bootstrap
  "Loads config from file and overrides from system env
   Provide args with :name, :env, :env-prefix and :config-dir"
  ([{:keys [name config-dir env-prefix env] :or
     {name "dev" config-dir "resources/config/" env-prefix nil env (System/getenv)}}]
     (let [file-cfg (read-config-file config-dir name)
           env-cfg (read-env-vars (set (keys file-cfg)) env-prefix env)]
       (reset! config-atom (merge {:name name} file-cfg env-cfg)))))

(def config
  (fn ([] @config-atom)
    ([& args] (apply @config-atom args))))
