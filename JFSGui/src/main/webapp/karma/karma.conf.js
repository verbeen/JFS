// Karma configuration
// Generated on Fri Dec 04 2015 15:42:54 GMT+0100 (CET)

module.exports = function(config) {
  config.set({

    // base path that will be used to resolve all patterns (eg. files, exclude)
    basePath: '../',


    // frameworks to use
    // available frameworks: https://npmjs.org/browse/keyword/karma-adapter
    frameworks: ['jasmine'],


    // list of files / patterns to load in the browser
    files: [
        'vendor/angularjs-1.4.7/angular.js',
        'vendor/angularjs-1.4.7/angular-mocks.js',
        'vendor/angularjs-1.4.7/angular-cookies.min.js',
        'vendor/angularjs-1.4.7/angular-route.min.js',
        'vendor/angularjs-1.4.7/angular-sanitize.min.js',
        'vendor/angular-strap-2.3.5/angular-strap.min.js',
        'vendor/angular-strap-2.3.5/angular-strap.tpl.min.js',
        'vendor/angular-masonry-0.13.0/angular-masonry.min.js',
        'app/app.js',
        'app/sample/sample.js',
        'app/sample/sample_test.js'
    ],


    // list of files to exclude
    exclude: [
    ],


    // preprocess matching files before serving them to the browser
    // available preprocessors: https://npmjs.org/browse/keyword/karma-preprocessor
    preprocessors: {
    },


    // test results reporter to use
    // possible values: 'dots', 'progress'
    // available reporters: https://npmjs.org/browse/keyword/karma-reporter
    reporters: ['progress'],


    // web server port
    port: 9876,


    // enable / disable colors in the output (reporters and logs)
    colors: true,


    // level of logging
    // possible values: config.LOG_DISABLE || config.LOG_ERROR || config.LOG_WARN || config.LOG_INFO || config.LOG_DEBUG
    logLevel: config.LOG_INFO,


    // enable / disable watching file and executing tests whenever any file changes
    autoWatch: true,


    // start these browsers
    // available browser launchers: https://npmjs.org/browse/keyword/karma-launcher
    browsers: ['Chrome'],


    // Continuous Integration mode
    // if true, Karma captures browsers, runs the tests and exits
    singleRun: false,

    // Concurrency level
    // how many browser should be started simultanous
    concurrency: Infinity
  })
}
