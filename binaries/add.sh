#!/bin/bash
# An Example Bash-Skript, which can be used to add the QUOTES to another
# CMS-System or do what ever you want with the quotes…

if [ "$1" ]; then
    echo "parameter: $1"
    # do something, e.g. adding your quote
else
    echo "Error: Missing parameter" 1>&2
    exit 1
fi
