package viaduct.demoapp.starwars.resolvers

import viaduct.api.Resolver
import viaduct.api.grts.Character
import viaduct.api.grts.Film
import viaduct.demoapp.starwars.data.StarWarsData

@Resolver("id")
class PlanetResidentsResolver : viaduct.demoapp.starwars.resolverbases.PlanetResolvers.Residents() {
    override suspend fun resolve(ctx: Context): List<Character?>? {
        val planetId = ctx.objectValue.getId().internalID
        val residents = StarWarsData.characters.filter { it.homeworldId == planetId }
        return residents.map { c ->
            Character.Builder(ctx)
                .id(ctx.globalIDFor(Character.Reflection, c.id))
                .name(c.name)
                .birthYear(c.birthYear)
                .eyeColor(c.eyeColor)
                .gender(c.gender)
                .hairColor(c.hairColor)
                .height(c.height)
                .mass(c.mass?.toDouble())
                .created(c.created.toString())
                .edited(c.edited.toString())
                .build()
        }
    }
}

@Resolver("id")
class PlanetFilmsResolver : viaduct.demoapp.starwars.resolverbases.PlanetResolvers.Films() {
    override suspend fun resolve(ctx: Context): List<Film?>? {
        val planetId = ctx.objectValue.getId().internalID

        val residentCharacterIds = StarWarsData.characters
            .filter { it.homeworldId == planetId }
            .map { it.id }
            .toSet()

        val filmIds = StarWarsData.characterFilmRelations
            .filter { (characterId, _) -> characterId in residentCharacterIds }
            .flatMap { it.value }
            .toSet()

        val films = StarWarsData.films.filter { it.id in filmIds }
        return films.map { f ->
            Film.Builder(ctx)
                .id(ctx.globalIDFor(Film.Reflection, f.id))
                .title(f.title)
                .episodeID(f.episodeID)
                .director(f.director)
                .producers(f.producers)
                .releaseDate(f.releaseDate)
                .created(f.created.toString())
                .edited(f.edited.toString())
                .build()
        }
    }
}
