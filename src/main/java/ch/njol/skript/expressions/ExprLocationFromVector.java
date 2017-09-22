/**
 *   This file is part of Skript.
 *
 *  Skript is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Skript is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Skript.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 * Copyright 2011-2017 Peter Güttinger and contributors
 */
package ch.njol.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.util.Vector;
import org.eclipse.jdt.annotation.Nullable;

/**
 * @author bi0qaw
 */
@Name("Vectors - Create location from vector")
@Description("Creates a location from a vector in a world")
@Examples({"set {_loc} to {_v} to location in world \"world\"",
		"set {_loc} to {_v} to location in world \"world\" with yaw 45 and pitch 90",
		"set {_loc} to location of {_v} in \"world\" with yaw 45 and pitch 90"})
@Since("2.2-dev28")
public class ExprLocationFromVector extends SimpleExpression<Location> {
	static {
		// TODO fix slowdowns and enable again, for now nuked for greater good
//		Skript.registerExpression(ExprLocationFromVector.class, Location.class, ExpressionType.SIMPLE,
//				"%vector% [to location] [in] %world%", "location (from|of) %vector% [(from|in)] %world%",
//				"%vector% [to location] [in] %world% with yaw %number% and pitch %number%",
//				"location (from|of) %vector% [(in|from)] %world% with yaw %number% and pitch %number%");
	}

	@SuppressWarnings("null")
	private Expression<Vector> vector;
	@SuppressWarnings("null")
	private Expression<World> world;
	@SuppressWarnings("null")
	private Expression<Number> yaw, pitch;
	private boolean yawpitch;

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends Location> getReturnType() {
		return Location.class;
	}

	@Override
	@SuppressWarnings({"unchecked", "null"})
	public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
		if (expressions.length > 3) {
			yawpitch = true;
		}
		vector = (Expression<Vector>)expressions[0];
		world = (Expression<World>)expressions[1];
		if (yawpitch) {
			yaw = (Expression<Number>)expressions[2];
			pitch = (Expression<Number>)expressions[3];
		}
		return true;
	}

	@SuppressWarnings("null")
	@Override
	protected Location[] get(Event event) {
		Vector v = vector.getSingle(event);
		World w = world.getSingle(event);
		Number y = yaw != null ? yaw.getSingle(event) : null;
		Number p = pitch != null ? pitch.getSingle(event) : null;
		if (v == null || w == null){
			return null;
		}
		if (y == null || p == null){
			return new Location[]{ v.toLocation(w)};
		}
		else {
			return new Location[]{v.toLocation(w, y.floatValue(), p.floatValue())};
		}
	}

	@Override
	public String toString(final @Nullable Event event, boolean b) {
		if (yawpitch) {
			return "location from " + vector.toString() + " with yaw " + yaw.toString() + " and pitch " + pitch.toString();
		}
		return "location from " + vector.toString();
	}
}
