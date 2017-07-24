FROM jekyll/jekyll:pages
EXPOSE 4000
ENTRYPOINT ["jekyll", "serve", "--watch", "--incremental"]
