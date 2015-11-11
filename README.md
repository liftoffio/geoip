# liftoff/geoip

At the moment this is a very trivial Clojure wrapper around [Maxmind's GeoIP Java
API](https://github.com/maxmind/geoip-api-java). It supports the country edition of their database.

# Usage
Add this dependency to your `:dependencies` array in your Lein .project file:

    [liftoff/geoip "0.1.0-SNAPSHOT"]

```clojure
(require '[geoip.core :as geoip])

(geoip/init-geoip "path/to/GeoIp.dat")
(geoip/lookup-country "12.207.22.244")
  => {:code "US", :name "United States"}

(geoip/init-geoip "path/to/GeoLiteCity.dat")
(geoip/lookup-location "12.207.22.244")
  => {:country-code "US", :country-name "United States", :region-code "CA", :region-name "California",
      :city "Palo Alto", :postal-code "94301", :latitude 37.441895, :longitude -122.143005}
```

## License

Distributed under the Eclipse Public License, the same as Clojure.
