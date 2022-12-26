FROM jekyll/jekyll:4.2.0
EXPOSE 4000
ENTRYPOINT ["jekyll", "serve", "--watch", "--incremental"]
