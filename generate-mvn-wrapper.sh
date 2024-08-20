#!/bin/bash

##
# The MIT License (MIT)
#
# Copyright (c) 2015-2023 Mickael Jeanroy
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in all
# copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
# SOFTWARE.
##

# mvn wrapper:wrapper

function clean_mvnw {
  local GREEN="\033[0;32m"
  local RESET_COLORS="\033[0m"

  echo -e "⌛ ${GREEN}Cleaning mvnw...${RESET_COLORS}"
  rm -f mvnw
  rm -f mvnw.cmd
  rm -rf .mvn
  echo -e "✅ ${GREEN}Cleaning done...${RESET_COLORS}"
  echo ""
}

function generate_mvnw {
  local GREEN="\033[0;32m"
  local RESET_COLORS="\033[0m"

  echo -e "⌛ ${GREEN}Cleaning mvnw...${RESET_COLORS}"
  mvn wrapper:wrapper
  echo -e "✅ ${GREEN}Cleaning done...${RESET_COLORS}"
  echo ""
}

clean_mvnw
generate_mvnw
