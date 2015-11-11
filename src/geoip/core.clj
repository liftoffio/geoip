; A thin clojure wrapper around Maxmind's java APIs which access its geoip database.
; Currently this handles only countries.
(ns geoip.core
  (:import [com.maxmind.geoip LookupService regionName]
           java.io.File
           java.util.Locale))

(set! *warn-on-reflection* true)

(def ^:private lookup-service (atom nil))

; NOTE: We define init-geoip as a multimethod so that we can provide the contract of accepting either a
; string or file while still preventing reflection.
(defmulti init-geoip
  "- database: the country edition of the maxmind geoip database. Can be either a File or a String."
  class)

(defmethod init-geoip String
  [^String database-path]
  (let [service (LookupService. database-path LookupService/GEOIP_MEMORY_CACHE)]
    (reset! lookup-service service)))

(defmethod init-geoip File
  [^File database-file]
  (let [service (LookupService. database-file LookupService/GEOIP_MEMORY_CACHE)]
    (reset! lookup-service service)))

(defn lookup-country
  "Lookup country information for an IP address. Only available when querying a GeoIP Country database.
  Returns a map of the following country info, or nil if none found:
  {:name, :code}"
  [^String ip]
  (when-let [result (.getCountry ^LookupService @lookup-service ip)]
    {:code (.getCode result) :name (.getName result)}))

(defn lookup-location
  "Lookup location information for an IP address. Only available when querying a GeoIP City database.
  Returns a map of the following location info, or nil if none found:
  {:country-code, :country-name, :region-code, :region-name, :city, :postal-code, :latitude, :longitude,
   :dma-code}"
  [^String ip]
  (when-let [result (.getLocation ^LookupService @lookup-service ip)]
    {:country-code (.countryCode result)
     :country-name (.countryName result)
     :region-code (.region result)
     :region-name (regionName/regionNameByCode (.countryCode result) (.region result))
     :city (.city result)
     :postal-code (.postalCode result)
     :latitude (.latitude result)
     :longitude (.longitude result)
     :dma-code (.dma_code result)}))
