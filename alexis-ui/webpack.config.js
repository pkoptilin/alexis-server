const path = require('path');
const UglifyJsPlugin = require("uglifyjs-webpack-plugin");
const MiniCssExtractPlugin = require("mini-css-extract-plugin");
const OptimizeCSSAssetsPlugin = require("optimize-css-assets-webpack-plugin")

module.exports = (env, options) => {

    const isProduction = options.mode === 'production';

    return {
        entry: ['babel-polyfill', './src/app.js'],
        output: {
            path: path.join(__dirname, 'public', 'dist'),
            filename: 'bundle.js'
        },
        optimization: {
            minimizer: [
                new UglifyJsPlugin({
                    cache: true,
                    parallel: true,
                    sourceMap: true
                }),
                new OptimizeCSSAssetsPlugin({})
            ]
        },
        plugins: [
            new MiniCssExtractPlugin({
                filename: "styles.css",
            })
        ],
        module: {
            rules: [{
                loaders: [
                    'babel-loader?presets[]=react,presets[]=env,presets[]=stage-0'
                ],
                test: /\.js$/,
                exclude: /node_modules/
            },
                {
                    test: /\.s?css$/,
                    use: [
                        MiniCssExtractPlugin.loader,
                        {
                            loader: "css-loader", options: {
                            sourceMap: true
                        }
                        }, {
                            loader: "sass-loader", options: {
                                sourceMap: true
                            }
                        }
                    ]
                }
            ]
        },
        devtool: isProduction ? 'source-map' : 'cheap-module-eval-source-map',
        devServer: {
            contentBase: path.join(__dirname, 'public'),
            open: true,
            historyApiFallback: true,
            publicPath: '/dist/'
        }
    };
};
