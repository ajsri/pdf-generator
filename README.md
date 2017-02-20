# PDF Generation Service
## Using Spring Boot & Flying Saucer

Simple service that takes a URL parameter, converts it into a PDF, and serves it to the user. Right now it's pretty picky about well-formed and valid HTML.


### Endpoints

#### [GET] /pdf?url=URL_TO_HTML_FILE
Returns a PDF of requested HTML file in-browser

#### [POST] /save

    {
        filename: filename without extension
        markup: string of HTML with quotes escaped
        save: not implemented yet
    }

### Images
Image sources must be absolute paths at the moment

### Todo
- Sanitize URL inputs
- Better error handling
- Unit tests (I guess)
- Add toggle for downloading file vs browser view
- Add support for relative image paths
- ~~Add ability to render from string rather than file~~
