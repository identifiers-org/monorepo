; <?php exit; ?> DO NOT REMOVE THIS LINE
; file automatically generated or modified by Matomo; you can manually override the default values in global.ini.php by redefining them in this file.
; This file is a partial copy in case a recovery is needed
[database]
host = "matomomysql"
username = "..."
password = "..."
dbname = "..."
tables_prefix = "..."
adapter = "..."
charset = "utf8mb4"

[General]
noreply_email_address = "..."
noreply_email_name = "idorg Matomo"
enable_trusted_host_check = 0
salt = "..."
trusted_hosts[] = "..."
cors_domains[] = "..."
enable_processing_unique_visitors_multiple_sites = 1
enable_processing_unique_visitors_day = 1
enable_processing_unique_visitors_week = 1
enable_processing_unique_visitors_month = 1
enable_processing_unique_visitors_year = 1
enable_processing_unique_visitors_range = 0

[Tracker]
exclude_requests = "url=&#36;%2Frest%2Fcollections%2F"
enable_fingerprinting_across_websites = 1


[mail]
transport = "..."
port = ...
host = "..."
type = "Login"
username = "..."
password = "..."
encryption = "tls"