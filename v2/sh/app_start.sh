#!/usr/bin/bash
java \
-Dlogs.trace=/app/data/logs/trace.log \
-Dlogs.error=/app/data/logs/error.log \
-jar v2-0.1.jar \
--spring.profiles.active=dev
