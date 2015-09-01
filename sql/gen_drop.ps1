$data = Get-Content "exchange.sql"
$drop_sql = "exchange.drop.sql"
if (Test-Path $drop_sql) {
    Clear-Content $drop_sql;
} else {
    New-Item $drop_sql -type file   
}
foreach ($line in $data)
{    
    if ($line -match 'view\s+([a-zA-Z_]*)\s+as'){
        Add-Content $drop_sql ("drop view " + $Matches[1] + ";");
    } 
}
foreach ($line in $data)
{    
    if ($line -match 'table\s+(tb_[a-zA-Z_]*).*constraint\s+([a-zA-Z_]*)\s+'){
        Add-Content $drop_sql ("alter table " + $Matches[1] + " drop constraint " + $Matches[2] + ";");
    } 

}
foreach ($line in $data)
{    
    if ($line -match 'table\s+(tb_[a-zA-Z_]*)\s*\('){        
        Add-Content $drop_sql ("drop table if exists " + $Matches[1] + ";");
    } 
}