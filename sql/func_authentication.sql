-- DROP FUNCTION newito.bo_create_refresh_token(varchar, varchar, varchar);

CREATE OR REPLACE FUNCTION newito.bo_create_refresh_token(in_token character varying, in_expiry_date character varying, in_username character varying)
 RETURNS character varying
 LANGUAGE plpgsql
AS $function$
declare
	v_count integer;
	v_username varchar;
	v_result varchar;
begin
	select count(0) into v_count
	from newito.sessions
	where username = in_username;

	if v_count > 0 then
		delete from newito.sessions
		where username = in_username;
	end if;

	insert into newito.sessions(token, expiry_date, username)
	values(in_token, to_timestamp(in_expiry_date, 'YYYY-MM-DD HH24:MI:SS'), in_username);

	return in_token;
end;
$function$
;

-- DROP FUNCTION newito.bo_find_user_by_username(varchar);

CREATE OR REPLACE FUNCTION newito.bo_find_user_by_username(in_username character varying)
 RETURNS character varying
 LANGUAGE plpgsql
AS $function$
declare
	v_count integer;
	v_callback integer;
	v_message varchar;
	v_data varchar;
	v_username varchar;
	v_password varchar;
	v_nama varchar;
	v_result varchar;
begin
	select count(0) into v_count
	from newito.ito_m_user;

	if v_count = 1 then
		select username, password, nama
		into v_username, v_password, v_nama
		from newito.ito_m_user
		where username = in_username;
	
		v_callback := 1;
		v_message := 'User berhasil ditemukan';
		v_data := v_username || '#' || v_password || '#' || v_nama;
		v_result := v_callback || '##' || v_message || '##' || v_data;
	else
		v_callback := -1;
		v_message := 'User tidak ditemukan';
		v_result := v_callback || '##' || v_message;
	end if;

	return v_result;
end;
$function$
;

-- DROP FUNCTION newito.bo_getlogin(varchar, varchar);

CREATE OR REPLACE FUNCTION newito.bo_getlogin(in_username character varying, in_password character varying)
 RETURNS character varying
 LANGUAGE plpgsql
AS $function$
declare
	v_count integer;
	v_callback integer;
	v_message varchar;
	v_data varchar;
	v_nama varchar;
	v_result varchar;
begin
	select count(0), nama into v_count, v_nama
	from newito.ito_m_user
	where username = in_username
	and password = in_password
	group by nama;

	if v_count = 1 then
		v_callback := 1;
		v_message := 'Login berhasil';
		v_data := in_username || '#' || v_nama;
		v_result := v_callback || '##' || v_message || '##' || v_data;
	else 
		v_callback := -1;
		v_message := 'Login gagal';
		v_result := v_callback || '##' || v_message;
	end if;

	return v_result;
end;
$function$
;

-- DROP FUNCTION newito.bo_update_expiry_date(varchar, varchar);

CREATE OR REPLACE FUNCTION newito.bo_update_expiry_date(in_token character varying, in_expiry_date character varying)
 RETURNS character varying
 LANGUAGE plpgsql
AS $function$
declare
	v_username varchar;
	v_password varchar;
	v_nama varchar;
	v_result varchar;
begin 	
	update newito.sessions
	set expiry_date = to_timestamp(in_expiry_date, 'YYYY-MM-DD HH24:MI:SS')
	where token = in_token;

	select s.username, imu.password, imu.nama 
	into v_username, v_password, v_nama
	from newito.sessions s
	inner join newito.ito_m_user imu on imu.username = s.username 
	where token = in_token;

	v_result := v_username || '#' || v_password || '#' || v_nama;
	return v_result;
end;
$function$
;

-- DROP FUNCTION newito.bo_verify_token(varchar);

CREATE OR REPLACE FUNCTION newito.bo_verify_token(in_token character varying)
 RETURNS character varying
 LANGUAGE plpgsql
AS $function$
declare
	v_count integer;
	v_callback integer;
	v_message varchar;
	v_is_token_valid boolean;
	v_result varchar;
begin 
	select count(0) into v_count
	from newito.sessions
	where token = in_token;

	if v_count > 0 then
		select to_char(expiry_date, 'YYYY-MM-DD HH24:MI:SS') > to_char(current_date, 'YYYY-MM-DD HH24:MI:SS')
		into v_is_token_valid
		from newito.sessions
		where token = in_token;
		
		if v_is_token_valid then
			v_callback := 1;
			v_message := 'Token valid.';
		else 
			v_callback := -1;
			v_message := in_token || ' token was expired. Please make new login request.';
		end if;
	else 
		v_callback := -1;
		v_message := 'Token not found.';
	end if;

	v_result := v_callback || '##' || v_message;

	return v_result;
end;
$function$
;