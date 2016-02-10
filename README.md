# ring-external-assets

The library allows using static assets located on the filesystem.

## Installation

Include the following dependency in your `project.clj` file:

## Usage

Require the middleware and add it to your handler.

```clojure
(require '[ring.middleware.external-assets
           :refer [wrap-extranl-resources]])

(def app
  (wrap-extranl-resources handler "/var/www" "/external"))

```

The middleware accepts the handler, followed by the filesystem path where the assets are found,
and the URI prefix for the assets.

The assets can then be referenced on the page using the prefix:

```xml
<script src="/external/js/app.js" type="text/javascript"></script>
```

The middleware will look for `/js/app.js` file relative to the `/var/www` folder and
return it as a response.


## License

Copyright © 2016 Dmitri Sotnikov

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
