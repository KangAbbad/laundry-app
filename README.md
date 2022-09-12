# Mini Project - Backend

## Laundry API

### Deskripsi

Aplikasi backend sederhana untuk transaksi jasa laundry

### Swagger API Docs

Link: [https://app.swaggerhub.com/apis-docs/KangAbbad/laundry-app/1.0.0](https://app.swaggerhub.com/apis-docs/KangAbbad/laundry-app/1.0.0)

### Google Slides

Link: [https://docs.google.com/presentation/d/1b03NiBEOqI9CYMN8FSVcFwusBeqjomuK6s4Gj_atWBI/edit?usp=sharing](https://docs.google.com/presentation/d/1b03NiBEOqI9CYMN8FSVcFwusBeqjomuK6s4Gj_atWBI/edit?usp=sharing)

### Postgres function

```postgresql
create or replace function today_revenue()
returns table(admin_id bigint, total_revenue numeric)
as
$$
begin
	return query select t.admin_id, sum(t.total_price) as total_revenue from transactions t where date(created_at) = current_date group by t.admin_id;
end
$$
language plpgsql;
```
